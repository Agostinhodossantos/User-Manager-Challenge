package user.app.com.network;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import user.app.com.models.User;

public class DataProvider {
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseRef = firebaseDatabase.getReference();
    String userRef = "users";

    public interface ResponseListener{
        void onSuccess(Object response);
        void onFailure(String message);
    }

    public void createUser(User user, ResponseListener responseListener) {
        databaseRef.child(userRef)
                .child(user.getId())
                .setValue(user)
                .addOnSuccessListener(unused ->
                        responseListener.onSuccess("User created successfully"))
                .addOnFailureListener(e ->
                        responseListener.onFailure(e.getMessage()));
    }

    public void getAllUsers(ResponseListener responseListener) {
        databaseRef.child(userRef).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<User> userList = new ArrayList<>();
                userList.clear();
                for (DataSnapshot objSnapshot : snapshot.getChildren()){
                     User user = objSnapshot.getValue(User.class);
                     userList.add(user);
                }
                responseListener.onSuccess(userList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                responseListener.onFailure(error.getMessage());
            }
        });
    }

    public void updateUserName(String userId, String newName, ResponseListener listener) {
        databaseRef.child(userRef).child(userId).child("name").setValue(newName)
                .addOnSuccessListener(unused ->
                        listener.onSuccess("Name updated successfully"))
                .addOnFailureListener(e ->
                listener.onFailure(e.getMessage()));
    }

    public void updateUserBio(String userId, String newBio, ResponseListener listener) {
        databaseRef.child(userRef).child(userId).child("biography").setValue(newBio)
                .addOnSuccessListener(unused ->
                        listener.onSuccess("Bio updated successfully"))
                .addOnFailureListener(e ->
                        listener.onFailure(e.getMessage()));
    }

    public void updateUserImg(String userId, String newImg, ResponseListener listener) {
        databaseRef.child(userRef).child(userId).child("imgUrl").setValue(newImg)
                .addOnSuccessListener(unused ->
                        listener.onSuccess("img updated successfully"))
                .addOnFailureListener(e ->
                        listener.onFailure(e.getMessage()));
    }

    public void removeUser(String id, ResponseListener responseListener) {
        databaseRef.child(userRef)
                .child(id)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                responseListener.onSuccess("user removed");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                responseListener.onFailure(e.getMessage());
            }
        });
    }

    public void removeAll(ResponseListener responseListener) {
        databaseRef.child(userRef).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                responseListener.onSuccess("all users removed");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                responseListener.onFailure(e.getMessage());
            }
        });
    }

    public void fileUpload(Uri passedURI, ResponseListener responseListener){
        String filename = UUID.randomUUID().toString();
        StorageReference ref = FirebaseStorage
                .getInstance()
                .getReference("users/profile/" + filename);


       new Runnable() {
            @Override
            public void run() {
                ref.putFile(passedURI).addOnSuccessListener(taskSnapshot ->
                        ref.getDownloadUrl().addOnSuccessListener(url ->
                                responseListener.onSuccess(url.toString())))
                        .addOnFailureListener(e ->
                        responseListener.onFailure(e.toString()));
            }
        }.run();
    }

}
