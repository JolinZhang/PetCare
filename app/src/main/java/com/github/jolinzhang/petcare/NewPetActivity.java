package com.github.jolinzhang.petcare;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.github.jolinzhang.model.DataRepository;
import com.github.jolinzhang.model.Pet;
import com.github.jolinzhang.model.PetForm;
import com.github.jolinzhang.petcare.databinding.NewPetActivityBinding;
import com.github.jolinzhang.util.Util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Shadow on 11/26/16.
 */

public class NewPetActivity extends AppCompatActivity {

    private PetForm petForm = new PetForm();

    private EditText idEditText;
    private EditText nameEditText;
    private EditText vetNameEditText;
    private EditText vetPhoneEditText;
    private EditText chipIdEditText;
    private EditText chipCompanyEditText;
    private EditText medicationsEditText;

    private EditText birthdayEditText;

    private Spinner speciesSpinner;

    DatePickerDialog birthdayPickerDialog;

    private ImageView avatarImageView;

    Uri avatarUri;

    RadioButton femaleButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_pet_activity);
        setTitle("New Pet");

        NewPetActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.new_pet_activity);
        binding.setPetForm(petForm);

        bindUI();

        /* Spinner. */
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.species_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        speciesSpinner.setAdapter(adapter);
        speciesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        petForm.setSpecies("Cat");
                        break;
                    case 1:
                        petForm.setSpecies("Dog");
                        break;
                    case 2:
                        petForm.setSpecies("Bird");
                        break;
                    case 3:
                        petForm.setSpecies("Rabbit");
                        break;
                    case 4:
                        petForm.setSpecies("Others");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        /* Birthday picker. */
        Calendar newCalendar = Calendar.getInstance();
        birthdayPickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM. dd, yyyy", Locale.US);
                petForm.setBirthday(newDate.getTime());
                birthdayEditText.setText("Birthday: " + dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        birthdayEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                birthdayPickerDialog.show();
            }
        });

        /* Avatar. */
        Util.getInstance().loadImage(petForm.getId(), avatarImageView, false);
        avatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);//one can be replaced with any action code
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.form_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_action:
                save();
                finish();
                break;
            case R.id.cancel_action:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void save() {
        petForm.setId(idEditText.getText().toString().toLowerCase());
        petForm.setName(nameEditText.getText().toString());
        petForm.setVetName(vetNameEditText.getText().toString());
        petForm.setVetPhone(vetPhoneEditText.getText().toString());
        petForm.setChipId(chipIdEditText.getText().toString());
        petForm.setChipCompany(chipCompanyEditText.getText().toString());
        petForm.setMedications(medicationsEditText.getText().toString());

        petForm.setFemale(femaleButton.isChecked());

        if (avatarUri != null) {
            Util.getInstance().uploadPicture(avatarUri, petForm.getId(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                }
            });
        }

        DataRepository.getInstance().createOrUpdatePet(petForm);
    }

    private void bindUI() {
        idEditText = (EditText) findViewById(R.id.new_pet_id);
        nameEditText = (EditText) findViewById(R.id.new_pet_name);
        vetNameEditText = (EditText) findViewById(R.id.new_pet_vet_name);
        vetPhoneEditText = (EditText) findViewById(R.id.new_pet_vet_phone);
        chipCompanyEditText = (EditText) findViewById(R.id.new_pet_chip_company);
        chipIdEditText = (EditText) findViewById(R.id.new_pet_chip_id);
        medicationsEditText = (EditText) findViewById(R.id.new_pet_medications);

        birthdayEditText = (EditText) findViewById(R.id.new_pet_birthday) ;

        speciesSpinner = (Spinner) findViewById(R.id.new_pet_species);

        avatarImageView = (ImageView) findViewById(R.id.new_pet_avatar);

        femaleButton = (RadioButton) findViewById(R.id.new_pet_female);
    }

    /* Image picker. */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            avatarUri = data.getData();
        }
    }

}
