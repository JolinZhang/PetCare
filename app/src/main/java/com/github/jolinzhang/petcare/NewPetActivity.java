package com.github.jolinzhang.petcare;

import android.app.DatePickerDialog;
import android.content.Intent;
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
import com.github.jolinzhang.util.Util;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Shadow on 11/26/16.
 */

public class NewPetActivity extends AppCompatActivity {

    private Pet pet = new Pet();

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
                        pet.setSpecies("Cat");
                        break;
                    case 1:
                        pet.setSpecies("Dog");
                        break;
                    case 2:
                        pet.setSpecies("Bird");
                        break;
                    case 3:
                        pet.setSpecies("Rabbit");
                        break;
                    case 4:
                        pet.setSpecies("Others");
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
                pet.setBirthday(newDate.getTime());
                birthdayEditText.setText("Birthday: " + Util.getInstance().dateFormatter().format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        birthdayEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                birthdayPickerDialog.show();
            }
        });

        /* Avatar. */
        Util.getInstance().loadImage(pet.getId(), avatarImageView, false);
        avatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);//one can be replaced with any action code
            }
        });

        String petId = getIntent().getStringExtra("pet_id");
        if (petId != null) {
            setPet(DataRepository.getInstance().getPet(petId));
        }
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
        pet.setId(idEditText.getText().toString().toLowerCase());
        pet.setName(nameEditText.getText().toString());
        pet.setVetName(vetNameEditText.getText().toString());
        pet.setVetPhone(vetPhoneEditText.getText().toString());
        pet.setChipId(chipIdEditText.getText().toString());
        pet.setChipCompany(chipCompanyEditText.getText().toString());
        pet.setMedications(medicationsEditText.getText().toString());

        pet.setFemale(femaleButton.isChecked());

        if (avatarUri != null) {
            Util.getInstance().uploadPicture(avatarUri, pet.getId(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                }
            });
        }

        DataRepository.getInstance().createOrUpdatePet(pet);
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

    /* Set pet. */
    public void setPet(Pet pet) {
        this.pet = pet;
        Util.getInstance().loadImage(pet.getId(), avatarImageView, false);
        idEditText.setText(pet.getId());
        nameEditText.setText(pet.getName());
        femaleButton.setChecked(pet.isFemale());
        switch (pet.getSpecies()) {
            case "Cat":
                speciesSpinner.setSelection(0);
                break;
            case "Dog":
                speciesSpinner.setSelection(1);
                break;
            case "Bird":
                speciesSpinner.setSelection(2);
                break;
            case "Rabbit":
                speciesSpinner.setSelection(3);
                break;
            case "Others":
                speciesSpinner.setSelection(4);
                break;
            default:
                break;

        }
        birthdayEditText.setText("Birthday: " + Util.getInstance().dateFormatter().format(pet.getBirthday()));
        vetPhoneEditText.setText(pet.getVetPhone());
        vetNameEditText.setText(pet.getVetName());
        medicationsEditText.setText(pet.getMedications());
        chipIdEditText.setText(pet.getChipId());
        chipCompanyEditText.setText(pet.getChipCompany());
    }

}
