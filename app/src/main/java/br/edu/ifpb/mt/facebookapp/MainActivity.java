package br.edu.ifpb.mt.facebookapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // CallbackManager gerencia todas as chamadas feitas na SDK do facebook a partir de uma activity ou fragmento
    private CallbackManager callbackManager;
    // LoginManager gerencia todas as operações relacionadas ao login e a permissões no facebook
    private LoginManager loginManager;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            button = (Button) findViewById(R.id.button_select);

            // Inicialização da SDK do Facebook, dessa forma, permitindo a integração do app Android com o facebook
            FacebookSdk.sdkInitialize(getApplicationContext());

            // Cria uma instância de CallbackManager
            callbackManager = CallbackManager.Factory.create();

            // Array com todas as permissões que esse app precisa
            List<String> permissoesNecessarias = Arrays.asList("publish_actions");

            loginManager = LoginManager.getInstance();

            // Informa para o LoginManager quais as permissões que esse aplicativo precisa
            loginManager.logInWithPublishPermissions(this, permissoesNecessarias);

            // Gerencia o resultado da tentativa de login
            loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sharePhotoToFacebook();
                            Toast.makeText(getApplicationContext(), "Compartilhado com sucesso", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancel() {
                    Toast.makeText(getApplicationContext(), "Compartilhamento cancelado", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(FacebookException exception) {
                    Toast.makeText(getApplicationContext(), "Falha ao compartilhar", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    /**
     * Método responsável por tornar um arquivo de imagem em um conteúdo compartilhável
     * e também compartilhar este conteúdo
     */
    private void sharePhotoToFacebook(){
        Bitmap img = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(img)
                .setCaption("Teste de api")
                .build();

        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        ShareApi.share(content, null);

    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        callbackManager.onActivityResult(requestCode, responseCode, data);
    }

}