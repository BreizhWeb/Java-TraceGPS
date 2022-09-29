package haupas.android.tracegps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.view.View;

import haupas.classes.*;
import android.os.AsyncTask;

import android.content.SharedPreferences;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    // les zones de saisie et les variables associées
    private EditText editTextConnecterPseudo;
    private EditText editTextConnecterMdp;
    private EditText editMdpOubliePseudo;
    private EditText editTextCreerComptePseudo;
    private EditText editTextCreerCompteAdrMail;
    private EditText editTextCreerCompteNumTel;
    private String pseudo;
    private String mdp;
    private String typeUtilisateur;       // "utilisateur" ou "administrateur"
    private String adrMail;
    private String numTel;
    // les cases à cocher
    private CheckBox caseMdpVisible;
    private CheckBox caseMemoriser;
    // les boutons
    private Button buttonGuide;
    private Button buttonCreerCompte;
    private Button buttonCreerCompteEnvoyer;
    private Button buttonConnecter;
    private Button buttonConnecterEnvoyer;
    private Button buttonMdpOublie;
    private Button buttonMdpOublieEnvoyer;
    private Button buttonQuitter;
    // les layouts
    private LinearLayout layoutConnecter;
    private LinearLayout layoutCreerCompte;
    private LinearLayout layoutMdpOublie;
    // le ProgressBar pour afficher le cercle de chargement
    private ProgressBar progressBar;
    // les zones d'affichage de message
    private TextView textViewGuide;
    private TextView textViewCreerCompte;
    private TextView textViewMdpOublie;
    private TextView textViewMessage;

    // la mémorisation des données se fait au moyen de couples clé/valeur :
    private final String KEY_PSEUDO = "pseudo";
    private final String KEY_MDP = "mdp";

    // le passage des données entre activités se fait au moyen des "extras" qui sont portés par les Intent.
    // un extra est une couple de clé/valeur
    // nous en utiliserons 3 ici, dont voici les 3 clés :
    private final String EXTRA_PSEUDO = "pseudo";
    private final String EXTRA_MDP = "mdp";
    private final String EXTRA_TYPE_UTILISATEUR = "typeUtilisateur";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // récupération des objets XML grâce à leur ID
        layoutConnecter = (LinearLayout) findViewById(R.id.main_layoutConnecter);
        layoutMdpOublie = (LinearLayout) findViewById(R.id.main_layoutMdpOublie);
        layoutCreerCompte = (LinearLayout) findViewById(R.id.main_layoutCreerCompte);

        editTextConnecterPseudo = (EditText) findViewById(R.id.main_editTextConnecterPseudo);
        editTextConnecterMdp = (EditText) findViewById(R.id.main_editTextConnecterMdp);
        editMdpOubliePseudo = (EditText) findViewById(R.id.main_editTextMdpOubliePseudo);
        editTextCreerComptePseudo = (EditText) findViewById(R.id.main_editTextCreerComptePseudo);
        editTextCreerCompteAdrMail = (EditText) findViewById(R.id.main_editTextCreerCompteAdrMail);
        editTextCreerCompteNumTel = (EditText) findViewById(R.id.main_editTextCreerCompteNumTel);

        caseMemoriser = (CheckBox) findViewById(R.id.main_checkBoxConnecterMemoriser);
        caseMemoriser.setChecked(true);       // case cochée au démarrage

        caseMdpVisible = (CheckBox) findViewById(R.id.main_checkBoxConnecterMdpVisible);
        caseMdpVisible.setChecked(false);  // case décochée au démarrage

        buttonGuide = (Button) findViewById(R.id.main_buttonGuide);
        buttonCreerCompte = (Button) findViewById(R.id.main_buttonCreerCompte);
        buttonCreerCompteEnvoyer = (Button) findViewById(R.id.main_buttonCreerCompteEnvoyer);
        buttonConnecter = (Button) findViewById(R.id.main_buttonConnecter);
        buttonConnecterEnvoyer = (Button) findViewById(R.id.main_buttonConnecterEnvoyer);
        buttonMdpOublie = (Button) findViewById(R.id.main_buttonMdpOublie);
        buttonMdpOublieEnvoyer = (Button) findViewById(R.id.main_buttonMdpOublieEnvoyer);
        buttonQuitter = (Button) findViewById(R.id.main_buttonQuitter);

        textViewGuide = (TextView) findViewById(R.id.main_textViewGuide);
        textViewCreerCompte = (TextView) findViewById(R.id.main_textViewCreerCompte);
        textViewMdpOublie = (TextView) findViewById(R.id.main_textViewMdpOublie);
        textViewMessage = (TextView) findViewById(R.id.main_textViewMessage);

        progressBar = (ProgressBar) findViewById(R.id.main_progressBar);

        // masque le cercle de chargement et certains éléments
        progressBar.setVisibility(View.GONE);
        layoutConnecter.setVisibility(View.GONE);
        layoutMdpOublie.setVisibility(View.GONE);
        layoutCreerCompte.setVisibility(View.GONE);
        textViewGuide.setVisibility(View.GONE);
        textViewMessage.setText("");

        // association d'un écouteur d'évenement à chaque bouton
        buttonGuide.setOnClickListener ( new buttonGuideClickListener());
        buttonCreerCompte.setOnClickListener ( new buttonCreerCompteClickListener());
        buttonCreerCompteEnvoyer.setOnClickListener ( new buttonCreerCompteEnvoyerClickListener());
        buttonConnecter.setOnClickListener ( new buttonConnecterClickListener());
        buttonConnecterEnvoyer.setOnClickListener ( new buttonConnecterEnvoyerClickListener());
        buttonMdpOublie.setOnClickListener ( new buttonMdpOublieClickListener());
        buttonMdpOublieEnvoyer.setOnClickListener ( new buttonMdpOublieEnvoyerClickListener());
        buttonQuitter.setOnClickListener ( new buttonQuitterClickListener());

        // association d'un écouteur d'évenement à la case caseMdpVisible
        caseMdpVisible.setOnClickListener ( new caseMdpVisibleClickListener());

        // Récupération des données de connexion dans le cas où elles ont été mémorisées lors de la dernière connexion
        SharedPreferences mesDonnees = getPreferences(MODE_PRIVATE);
        pseudo = mesDonnees.getString(KEY_PSEUDO, "");
        mdp = mesDonnees.getString(KEY_MDP, "");
        // si des données avaient été mémorisées, elles sont proposées dans les zones de saisies
        if ( ! pseudo.equals("") ) editTextConnecterPseudo.setText(pseudo);
        if ( ! mdp.equals("") ) editTextConnecterMdp.setText(mdp);

    }

    /** classe interne pour gérer le clic sur le bouton buttonGuide. */
    private class buttonGuideClickListener implements View.OnClickListener {
        public void onClick(View v) {
            layoutConnecter.setVisibility(View.GONE);
            layoutCreerCompte.setVisibility(View.GONE);
            layoutMdpOublie.setVisibility(View.GONE);
            textViewMessage.setText("");

            if (textViewGuide.getVisibility() == View.GONE)
                textViewGuide.setVisibility(View.VISIBLE);  // afficher le texte explicatif
            else
                textViewGuide.setVisibility(View.GONE);     // masquer le texte explicatif

            String msg = "A QUOI ÇA SERT ?\n";
            msg += "Cette application permet à un utilisateur isolé (randonneur, coureur, vététiste, ...) de transmettre régulièrement sa position pour permettre";
            msg += " à un autre utilisateur (autorisé) de le localiser en cas de problème (malaise, chute, ...) et de non réponse au téléphone.\n\n";
            msg += "POURQUOI CREER UN COMPTE ?\n";
            msg +="L'utilisateur qui veut être localisé et l'utilisateur qui sera autorisé à le suivre doivent se créer chacun un compte avec un pseudo et une adresse mail.";
            msg += " Lors de la création du compte, le système crée un mot de passe et l'envoie par courriel à l'utilisateur (qui pourra bien sûr le modifier).";
            msg += " L'utilisateur suiveur devra ensuite demander l'autorisation de suivre un autre utilisateur.\n\n";
            msg += "COMMENT TRACER UN UTILISATEUR ?\n";
            msg += "L'utilisateur qui veut être localisé pendant son parcours choisit l'option 'Démarrer enregistrement'. et choisit la fréquence d'envoi.";
            msg += " L'utilisateur qui veut suivre le parcours d'un autre utilisateur choisit l'option 'Consulter un parcours'.\n";
            textViewGuide.setText(msg);
        }
    }

    // ----------------------------------------------------------------------------------------------------
    // ------------------------------- Tâche asynchrone TacheCreerUtilisateur ---------------------------------
    // ----------------------------------------------------------------------------------------------------

    // appel du service web CreerUtilisateur
    private class TacheCreerUtilisateur extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            // cette méthode exécute un traitement initial avant de lancer la tâche longue
            progressBar.setVisibility(View.VISIBLE);    // démarre le cercle de chargement
        }
        protected String doInBackground(Void... params) {
            // cette méthode permet de lancer l'exécution de la tâche longue
            String msg = PasserelleServicesWebXML.creerUnUtilisateur(pseudo, adrMail, numTel);
            return msg;
        }
        protected void onPostExecute(String msg) {
            // cette méthode est automatiquement appelée quand la tâche longue se termine
            textViewMessage.setText(msg);
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);  // arrête le cercle de chargement
        }
    } // fin tâche asynchrone TacheCreerUtilisateur

    // -----------------------------------------------------------------------------------------------------------------------
    // -------------------------------- Tâche asynchrone TacheConnecter -----------------------------------
    // -----------------------------------------------------------------------------------------------------------------------

    // appel du service web Connecter
    private class TacheConnecter extends AsyncTask<Void, Void, String> {
        protected void onPreExecute() {
            // cette méthode exécute un traitement initial avant de lancer la tâche longue
            progressBar.setVisibility(View.VISIBLE);    // démarre le cercle de chargement
        }
        protected String doInBackground(Void... params) {
            // cette méthode permet de lancer l'exécution de la tâche longue
            String msg = PasserelleServicesWebXML.connecter(pseudo, Outils.sha1(mdp));
            return msg;
        }
        protected void onPostExecute(String msg) {
            // cette méthode est automatiquement appelée quand la tâche longue se termine
            progressBar.setVisibility(View.GONE);	// arrête le cercle de chargement

            if (msg.startsWith("Erreur")) {
                textViewMessage.setText(msg);
            }
            else {
                // détermine le type d'utilisateur
                if (msg.startsWith("Utilisateur authentifié")) typeUtilisateur = "utilisateur";
                if (msg.startsWith("Administrateur authentifié")) typeUtilisateur = "administrateur";

                // efface le dernier message
                textViewMessage.setText("");

                // crée une Intent pour lancer le menu général
                Intent uneIntent = new Intent(MainActivity.this, MenuGeneral.class);
                // passe nom, mdp et typeUtilisateur à l'Intent
                uneIntent.putExtra(EXTRA_PSEUDO, pseudo);
                uneIntent.putExtra(EXTRA_MDP, mdp);
                uneIntent.putExtra(EXTRA_TYPE_UTILISATEUR, typeUtilisateur);
                // démarre l'activité à partir de l'Intent
                startActivity(uneIntent);

            }
        }
    } // fin tâche asynchrone TacheConnecter

    // ------------------------------------------------------------------------------------------------------------------------
    // -------------------------------- Tâche asynchrone TacheDemanderMdp ---------------------------------
    // ------------------------------------------------------------------------------------------------------------------------

    // appel du service web DemanderMdp
    private class TacheDemanderMdp extends AsyncTask<Void, Void, String> {
        protected void onPreExecute() {
            // cette méthode exécute un traitement initial avant de lancer la tâche longue
            progressBar.setVisibility(View.VISIBLE);    // démarre le cercle de chargement
        }
        protected String doInBackground(Void... params) {
            // cette méthode permet de lancer l'exécution de la tâche longue
            String msg = PasserelleServicesWebXML.demanderMdp(pseudo);
            return msg;
        }
        protected void onPostExecute(String msg) {
            // cette méthode est automatiquement appelée quand la tâche longue se termine
            textViewMessage.setText(msg);
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

            progressBar.setVisibility(View.GONE);	// arrête le cercle de chargement
        }
    } // fin tâche asynchrone TacheDemanderMdp


    /** classe interne pour gérer le clic sur le bouton buttonCreerCompte. */
    private class buttonCreerCompteClickListener implements View.OnClickListener {
        public void onClick(View v) {
            layoutConnecter.setVisibility(View.GONE);
            layoutMdpOublie.setVisibility(View.GONE);
            textViewGuide.setVisibility(View.GONE);
            textViewMessage.setText("");
            String msg = "Choisissez un pseudo (au moins 8 caractères), indiquez votre adresse mail";
            msg += " (obligatoire) votre n° de téléphone (facultatif) et validez avec le bouton Envoyer.";
            textViewCreerCompte.setText(msg);

            if (layoutCreerCompte.getVisibility() == View.GONE)
                layoutCreerCompte.setVisibility(View.VISIBLE);    // afficher le layout
            else
                layoutCreerCompte.setVisibility(View.GONE);       // masquer le layout

        }
    }

    /** classe interne pour gérer le clic sur le bouton buttonCreerCompteEnvoyer. */
    private class buttonCreerCompteEnvoyerClickListener implements View.OnClickListener {
        public void onClick(View v) {
            // récupération des données saisies
            pseudo = editTextCreerComptePseudo.getText().toString().trim();
            adrMail = editTextCreerCompteAdrMail.getText().toString().trim();
            numTel = editTextCreerCompteNumTel.getText().toString().trim();

            // test des données obligatoires
            if ( pseudo.equals("") || adrMail.equals("") ) {
                String msg = "Données incomplètes";
                textViewMessage.setText(msg);
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
            else {
                // appel du service web CreerUtilisateur avec une tâche asynchrone
                new TacheCreerUtilisateur().execute();
            }

        }
    }

    /** classe interne pour gérer le clic sur le bouton buttonConnecter. */
    private class buttonConnecterClickListener implements View.OnClickListener {
        public void onClick(View v) {
            layoutMdpOublie.setVisibility(View.GONE);
            layoutCreerCompte.setVisibility(View.GONE);
            textViewGuide.setVisibility(View.GONE);
            textViewMessage.setText("");

            if (layoutConnecter.getVisibility() == View.GONE)
                layoutConnecter.setVisibility(View.VISIBLE);    // afficher le layout
            else
                layoutConnecter.setVisibility(View.GONE);       // masquer le layout

        }
    }

    /** classe interne pour gérer le clic sur le bouton buttonConnecterEnvoyer. */
    private class buttonConnecterEnvoyerClickListener implements View.OnClickListener {
        public void onClick(View v) {
            // Mémorisation des données de connexion si la case est cochée
            SharedPreferences mesDonnees = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor monEditor = mesDonnees.edit();
            if (caseMemoriser.isChecked()) {
                monEditor.putString(KEY_PSEUDO, pseudo);
                monEditor.putString(KEY_MDP, mdp);
            }
            else {
                monEditor.putString(KEY_PSEUDO, "");
                monEditor.putString(KEY_MDP, "");
            }
            monEditor.commit();

            // appel du service web Connecter avec une tâche asynchrone
            new TacheConnecter().execute();
        }


    }

    /** classe interne pour gérer le clic sur le bouton buttonMdpOublie. */
    private class buttonMdpOublieClickListener implements View.OnClickListener {
        public void onClick(View v) {
            layoutCreerCompte.setVisibility(View.GONE);
            layoutConnecter.setVisibility(View.GONE);
            textViewGuide.setVisibility(View.GONE);
            textViewMessage.setText("");
            String msg = "Pour confirmer que vous souhaitez obtenir un nouveau mot de passe, ";
            msg += "saisissez votre pseudo.";
            textViewMdpOublie.setText(msg);

            if (layoutMdpOublie.getVisibility() == View.GONE)
                layoutMdpOublie.setVisibility(View.VISIBLE);    // afficher le layout
            else
                layoutMdpOublie.setVisibility(View.GONE);       // masquer le layout
        }
}


        /** classe interne pour gérer le clic sur le bouton buttonMdpOublieEnvoyer. */
        private class buttonMdpOublieEnvoyerClickListener implements View.OnClickListener {
            public void onClick(View v) {
                pseudo = editMdpOubliePseudo.getText().toString();
                if ( pseudo.equals("")) {
                    String msg = "Pour confirmer que vous souhaitez obtenir un nouveau mot de passe, ";
                    msg += "saisissez votre pseudo.";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    editMdpOubliePseudo.setHint("Mon pseudo");
                }
                else {
                    // appel du service web DemanderMdp avec une tâche asynchrone
                    new TacheDemanderMdp().execute();
                }
            }
        }


    /** classe interne pour gérer le clic sur le bouton buttonQuitter. */
    private class buttonQuitterClickListener implements View.OnClickListener {
        public void onClick(View v) {
            finish();
        }
    }

    /** classe interne pour gérer le clic sur la case caseMdpVisible. */
    private class caseMdpVisibleClickListener implements View.OnClickListener {
        public void onClick(View v) {
            if (caseMdpVisible.isChecked())
                editTextConnecterMdp.setInputType(android.text.InputType.TYPE_CLASS_TEXT |
                        android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            else
                editTextConnecterMdp.setInputType(android.text.InputType.TYPE_CLASS_TEXT |
                        android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }

    }

} // fin de l'activité

