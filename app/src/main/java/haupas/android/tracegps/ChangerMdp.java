package haupas.android.tracegps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.view.View;
import android.content.Intent;

import android.os.AsyncTask;
import android.widget.Toast;
import haupas.classes.Outils;
import haupas.classes.PasserelleServicesWebXML;


public class ChangerMdp extends AppCompatActivity {

    // les zones de saisie et les variables associées
    private EditText editTextMdp;
    private EditText editTextConfirmationMdp;

    private String nouveauMdp;
    private String confirmation;

    // les 2 boutons
    private Button buttonEnvoyer;
    private Button buttonRetourner;

    // le ProgressBar pour afficher le cercle de chargement
    private ProgressBar progressBar;

    // les zones d'affichage de message
    private TextView textViewGuide;
    private TextView textViewMessage;

    // le passage des données entre activités se fait au moyen des "extras" qui sont portés par les Intent.
    // un extra est une couple de clé/valeur
    // nous en utiliserons 2 ici, dont voici les 2 clés et les 2 variables associées :
    private final String EXTRA_PSEUDO = "pseudo";
    private final String EXTRA_MDP = "mdp";
    private String nom;
    private String mdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changer_mdp);

        // récupération du nom, et du mot de passe passés par l'activité précédente
        Intent uneIntent = getIntent();
        nom = uneIntent.getStringExtra(EXTRA_PSEUDO);
        mdp = uneIntent.getStringExtra(EXTRA_MDP);

        // récupération des EditText grâce à leur ID
        editTextMdp = (EditText) findViewById(R.id.changer_mdp_editTextMdp);
        editTextConfirmationMdp = (EditText) findViewById(R.id.changer_mdp_editTextConfirmationMdp);

        // récupération des Button grâce à leur ID
        buttonEnvoyer = (Button) findViewById(R.id.changer_mdp_buttonEnvoyer);
        buttonRetourner = (Button) findViewById(R.id.changer_mdp_buttonRetourner);

        // récupération des TextView grâce à leur ID et initialisations des textes affichés
        textViewGuide = (TextView) findViewById(R.id.changer_mdp_textViewGuide);
        textViewMessage = (TextView) findViewById(R.id.changer_mdp_textViewMessage);
        String msg = "\nChoisissez votre nouveau mot de passe (au moins 8 caractères), et confirmez-le.\n";
        msg += "Puis validez avec le bouton Envoyer.\n";
        textViewGuide.setText(msg);
        textViewMessage.setText("");

        progressBar = (ProgressBar) findViewById(R.id.changer_mdp_progressBar);
        // arrête le cercle de chargement
        progressBar.setVisibility(View.GONE);

        // association d'un écouteur d'évenement à chaque bouton
        buttonEnvoyer.setOnClickListener ( new buttonEnvoyerClickListener());
        buttonRetourner.setOnClickListener ( new buttonRetournerClickListener());
    } // fin de onCreate

    /** classe interne pour gérer le clic sur le bouton buttonEnvoyer. */
    private class buttonEnvoyerClickListener implements View.OnClickListener {
        public void onClick(View v) {
        }
    }

    /** classe interne pour gérer le clic sur le bouton buttonRetourner. */
    private class buttonRetournerClickListener implements View.OnClickListener {
        public void onClick(View v) {
            // crée une Intent pour retourner nouveauMdp à l'activité MenuGeneral
            Intent uneIntent = new Intent();
            // passe les données à l'Intent
            uneIntent.putExtra(EXTRA_MDP, mdp);
            setResult(RESULT_OK, uneIntent);
            finish();

        }
    }

} // fin de l'activité

