package things.useful.databaza2;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.CharacterPickerDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText editName;
    EditText editSurname;
    EditText editMarks;
    EditText editId;

    Button butGen;
    Button butViewAll;
    Button butUpdate;
    Button butDelete;
    Button prikazi_po_id;
    Button prikazi_listu_sa_sadrzinom_u_imenu;
    Button but_info;
    Button but_clear;

    SQLliteHELP myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb = new SQLliteHELP(this);

        editName = (EditText) findViewById(R.id.editText_name);
        editSurname = (EditText) findViewById(R.id.editText_surname);
        editMarks = (EditText) findViewById(R.id.editText_marks);
        editId = (EditText) findViewById(R.id.editText_id);

        butGen = (Button) findViewById(R.id.Add_data);
        butViewAll = (Button) findViewById(R.id.but_GetAll);
        butUpdate = (Button) findViewById(R.id.but_update);
        butDelete = (Button) findViewById(R.id.butDelete);
        prikazi_po_id = (Button) findViewById(R.id.prikazi_po_id);
        prikazi_listu_sa_sadrzinom_u_imenu = (Button) findViewById(R.id.prikazi_listu_sa_sadrzinom_u_imenu);
        but_info = (Button) findViewById(R.id.but_info);
        but_clear = (Button) findViewById(R.id.but_clear);

        addData();
        viewAll();
        update();
        deleteData();
        pronadjiID();
        pronadjiImena();
        info(this);
        clear();

    }

    public void addData(){
        butGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInserted;
                if(!editName.getText().toString().isEmpty()){
                    if(!editSurname.getText().toString().isEmpty()){
                        if(!editMarks.getText().toString().isEmpty()){
                            boolean flag = true;
                            for(int i = 0; i < editMarks.getText().toString().length(); i++){
                                if(!Character.isDigit(editMarks.getText().toString().charAt(i))){
                                    Toast.makeText(MainActivity.this, "Samo broj od 6 do 10 se može uneti.", Toast.LENGTH_LONG).show();
                                    flag = false;
                                    break;
                                }
                            }
                            if(flag){
                                int konvert = Integer.parseInt(editMarks.getText().toString());
                                if(konvert >= 6 && konvert <= 10){
                                    isInserted = myDb.insertData(editName.getText().toString(),
                                            editSurname.getText().toString(),
                                            editMarks.getText().toString());
                                    if(isInserted == true){
                                        Toast.makeText(MainActivity.this, "Podatci su uneti", Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(MainActivity.this, "Podatci nisu uneti", Toast.LENGTH_LONG).show();
                                    }
                                }else{
                                    Toast.makeText(MainActivity.this, "Morate uneti broj između 6 i 10.", Toast.LENGTH_LONG).show();
                                }

                            }
                        }else{
                            Toast.makeText(MainActivity.this, "Polje ocena nesme ostati prazno.", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(MainActivity.this, "Polje prezime nesme ostati prazno.", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this, "Polje ime nesme ostati prazno.", Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    public void viewAll(){
        butViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = myDb.getAllData();
                if(res.getCount() == 0){
                    //ako je ovo onda nema nista u tabeli
                    showMessage("Greška", "Ništa nije pronađeno.");
                    return;
                }

                StringBuffer buffer = new StringBuffer();
                while(res.moveToNext()){
                    buffer.append("Id :"+ res.getString(0)+"\n");
                    buffer.append("Ime :"+ res.getString(1)+"\n");
                    buffer.append("Prezime :"+ res.getString(2)+"\n");
                    buffer.append("Ocena :"+ res.getString(3)+"\n\n");
                }

                //Show all data
                showMessage("Data", buffer.toString());
            }
        });
    }

    public void pronadjiID() {
        prikazi_po_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = true;

                if(!editId.getText().toString().isEmpty()){
                    for(int i = 0; i < editId.getText().toString().length(); i++) {
                        if (!Character.isDigit(editId.getText().toString().charAt(i))) {
                            flag = false;
                            break;
                        }
                    }
                    if(flag){
                        Cursor res = myDb.getDataByID(editId.getText().toString());
                        if (res.getCount() == 0) {
                            //ako je ovo onda nema nista u tabeli
                            showMessage("Greška", "Ništa nije pronađeno.");
                            return;
                        }

                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()) {
                            buffer.append("Id :"+ res.getString(0)+"\n");
                            buffer.append("Ime :"+ res.getString(1)+"\n");
                            buffer.append("Prezime :"+ res.getString(2)+"\n");
                            buffer.append("Ocena :"+ res.getString(3)+"\n\n");
                        }

                        //Show all data
                        showMessage("Data", buffer.toString());
                    }else{
                        Toast.makeText(MainActivity.this, "Morate uneti broj.", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this, "Morate uneti broj.", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public void pronadjiImena(){
        prikazi_listu_sa_sadrzinom_u_imenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = myDb.getName(editName.getText().toString());
                if (res.getCount() == 0) {
                    //ako je ovo onda nema nista u tabeli
                    showMessage("Greška", "Ništa nije pronađeno.");
                    return;
                }

                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()) {
                    buffer.append("Id :"+ res.getString(0)+"\n");
                    buffer.append("Ime :"+ res.getString(1)+"\n");
                    buffer.append("Prezime :"+ res.getString(2)+"\n");
                    buffer.append("Ocena :"+ res.getString(3)+"\n\n");
                }

                //Show all data
                showMessage("Data", buffer.toString());
            }
        });
    }


    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                closeContextMenu();
            }
        });
        builder.show();
    }

    public void update(){
        butUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isUpdated = myDb.updateData(editId.getText().toString(),
                        editName.getText().toString(),
                        editSurname.getText().toString(),
                        editMarks.getText().toString());
                if(isUpdated==true){
                    Toast.makeText(MainActivity.this, "Podatci su Update-ovani", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MainActivity.this, "Podatci nisu Update-ovani", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void deleteData(){
        butDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Integer deletedRows = myDb.deleteData(editId.getText().toString());
                if(deletedRows > 0){
                    Toast.makeText(MainActivity.this, "Podatci su obrisani", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MainActivity.this, "Podatci nisu obrisani", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void info(final Context context){
        but_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Informacije o aplikaciji.")
                        .setMessage(R.string.info)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                closeContextMenu();
                            }
                        })
                        .setIcon(R.drawable.ic_action_name)
                        .show();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String st1 = editName.getText().toString();
        String st2 = editSurname.getText().toString();
        String st3 = editMarks.getText().toString();
        String st4 = editId.getText().toString();

        outState.putString("s1", st1);
        outState.putString("s2", st2);
        outState.putString("s3", st3);
        outState.putString("s4", st4);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String st1 = savedInstanceState.getString("s1");
        String st2 = savedInstanceState.getString("s2");
        String st3 = savedInstanceState.getString("s3");
        String st4 = savedInstanceState.getString("s4");

        editName.setText(st1);
        editSurname.setText(st2);
        editMarks.setText(st3);
        editId.setText(st4);

    }

    public void clear(){
        but_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editName.setText("");
                editSurname.setText("");
                editMarks.setText("");
                editId.setText("");
            }
        });
    }
}
