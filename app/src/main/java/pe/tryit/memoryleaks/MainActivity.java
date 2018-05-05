package pe.tryit.memoryleaks;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    static Activity activity = null;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mPersonMovieDR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Static activity reference leak
        if (activity == null) {
            activity = this;
        }

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        //Uncomment to start listener leak
        startListeningFromFirebase();

    }

    private void startListeningFromFirebase() {
        /*mDatabaseReference.child("person-movies").addValueEventListener(
                new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("MainActivity","Llego un valor");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        //Uncomment to solve listener leak
        mPersonMovieDR = mDatabaseReference.child("person-movies");
        mPersonMovieDR.addValueEventListener(movieValueEventListener);
    }

    private ValueEventListener movieValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Log.d("MainActivity","Llego un valor");
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Uncomment to solve static activity leak
        activity = null;

        //Uncomment to solve listener leak
        mPersonMovieDR.removeEventListener(movieValueEventListener);
    }
}
