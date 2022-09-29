package haupas.android.tracegps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import android.content.Intent;

public class MenuGeneral extends AppCompatActivity {

    // les boutons
    private Button buttonTesterGeolocalisation;
    private Button buttonDemanderAutorisation;
    private Button buttonRetirerAutorisation;
    private Button buttonDemarrerEnregistrement;
    private Button buttonConsulterParcours;
    private Button buttonSupprimerParcours;
    private Button buttonChangerMdp;
    private Button buttonConsulterUtilisateurs;
    private Button buttonSupprimerUtilisateur;
    private Button buttonDeconnecter;
    // le passage des données entre activités se fait au moyen des "extras" qui sont portés par les Intent.
    // un extra est une couple de clé/valeur
    // nous en utiliserons 3 ici, dont voici les 3 clés et les 3 variables associées :
    private final String EXTRA_PSEUDO = "pseudo";
    private final String EXTRA_MDP = "mdp";
    private final String EXTRA_TYPE_UTILISATEUR = "typeUtilisateur";
    private String pseudo;
    private String mdp;
    private String typeUtilisateur;       // "utilisateur" ou "administrateur"

    // codes utilisés avec la méthode startActivityForResult
    // ces nombres peuvent être quelconques, mais doivent être différents les uns des autres
    private final int CODE_RESULTAT_CHANGEMENT_MDP = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_general);

        // récupération des Button grâce à leur ID
        buttonTesterGeolocalisation = (Button) findViewById(R.id.menu_bouton_tester_geolocalisation);
        buttonDemanderAutorisation = (Button) findViewById(R.id.menu_bouton_demander_autorisation);
        buttonRetirerAutorisation = (Button) findViewById(R.id.menu_bouton_retirer_autorisation);
        buttonDemarrerEnregistrement = (Button) findViewById(R.id.menu_bouton_demarrer_enregistrement);
        buttonConsulterParcours = (Button) findViewById(R.id.menu_bouton_consulter_parcours);
        buttonSupprimerParcours = (Button) findViewById(R.id.menu_bouton_supprimer_parcours);
        buttonChangerMdp = (Button) findViewById(R.id.menu_bouton_changer_mdp);
        buttonConsulterUtilisateurs = (Button) findViewById(R.id.menu_bouton_lister_utilisateurs);
        buttonSupprimerUtilisateur = (Button) findViewById(R.id.menu_bouton_supprimer_utilisateur);
        buttonDeconnecter = (Button) findViewById(R.id.menu_bouton_deconnecter);

        // association d'un écouteur d'évenement à chaque bouton
        buttonTesterGeolocalisation.setOnClickListener ( new buttonTesterGeolocalisationClickListener());
        buttonDemanderAutorisation.setOnClickListener ( new buttonDemanderAutorisationClickListener());
        buttonRetirerAutorisation.setOnClickListener ( new buttonRetirerAutorisationClickListener());
        buttonDemarrerEnregistrement.setOnClickListener ( new buttonDemarrerEnregistrementClickListener());
        buttonConsulterParcours.setOnClickListener ( new buttonConsulterParcoursClickListener());
        buttonSupprimerParcours.setOnClickListener ( new buttonSupprimerParcoursClickListener());
        buttonChangerMdp.setOnClickListener ( new buttonChangerMdpClickListener());
        buttonConsulterUtilisateurs.setOnClickListener ( new buttonConsulterUtilisateursClickListener());
        buttonSupprimerUtilisateur.setOnClickListener ( new buttonSupprimerUtilisateurClickListener());
        buttonDeconnecter.setOnClickListener ( new buttonDeconnecterClickListener());

        // récupération du nom, du mot de passe et du type d'utilisateur passés par l'activité précédente
        Intent uneIntent = getIntent();
        pseudo = uneIntent.getStringExtra(EXTRA_PSEUDO);
        mdp = uneIntent.getStringExtra(EXTRA_MDP);
        typeUtilisateur = uneIntent.getStringExtra(EXTRA_TYPE_UTILISATEUR);

        // boutons buttonConsulterUtilisateurs et buttonSupprimerUtilisateur visibles seulement pour un administrateur
        if ( typeUtilisateur.equals("utilisateur") ) {
            // masquer les 2 boutons
            buttonConsulterUtilisateurs.setVisibility(View.GONE);
            buttonSupprimerUtilisateur.setVisibility(View.GONE);
        }

    }

    /** récupére les données fournies par une activité appelée avec startActivityForResult
     *   ici, une seule activité concernée : celle qui permet de changer le mot de passe
     *   il faut alors récupérer ce nouveau mot de passe pour pouvoir l'utiliser lors des appels de services web
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent uneIntent) {
        super.onActivityResult(requestCode, resultCode, uneIntent);

        switch (requestCode) {
            case CODE_RESULTAT_CHANGEMENT_MDP :
                mdp = uneIntent.getStringExtra(EXTRA_MDP); break;
        }
    }


    /** classe interne pour gérer le clic sur le bouton buttonTesterGeolocalisation. */
    private class buttonTesterGeolocalisationClickListener implements View.OnClickListener {
        public void onClick(View v) {
        }
    }

    /** classe interne pour gérer le clic sur le bouton buttonDemanderAutorisation. */
    private class buttonDemanderAutorisationClickListener implements View.OnClickListener{
        public void onClick(View v) {
        }
    }

    /** classe interne pour gérer le clic sur le bouton buttonRetirerAutorisation. */
    private class buttonRetirerAutorisationClickListener implements View.OnClickListener{
        public void onClick(View v) {
        }
    }

    /** classe interne pour gérer le clic sur le bouton buttonDemarrerEnregistrement. */
    private class buttonDemarrerEnregistrementClickListener implements View.OnClickListener{
        public void onClick(View v) {
        }
    }

    /** classe interne pour gérer le clic sur le bouton buttonConsulterParcours. */
    private class buttonConsulterParcoursClickListener implements View.OnClickListener{
        public void onClick(View v) {
        }
    }

    /** classe interne pour gérer le clic sur le bouton buttonSupprimerParcours. */
    private class buttonSupprimerParcoursClickListener implements View.OnClickListener{
        public void onClick(View v) {
        }
    }

    /** classe interne pour gérer le clic sur le bouton buttonChangerMdp. */
    private class buttonChangerMdpClickListener implements View.OnClickListener{
        public void onClick(View v) {
            // crée une Intent pour lancer l'activité
            Intent uneIntent = new Intent(MenuGeneral.this, ChangerMdp.class);
            // passe nom, mdp et typeUtilisateur à l'Intent
            uneIntent.putExtra(EXTRA_PSEUDO, pseudo);
            uneIntent.putExtra(EXTRA_MDP, mdp);
            uneIntent.putExtra(EXTRA_TYPE_UTILISATEUR, typeUtilisateur);
            // démarre l'activité à partir de l'Intent et attend un résultat (le nouveau mdp)
            startActivityForResult(uneIntent, CODE_RESULTAT_CHANGEMENT_MDP);

        }
    }

    /** classe interne pour gérer le clic sur le bouton buttonConsulterUtilisateurs. */
    private class buttonConsulterUtilisateursClickListener implements View.OnClickListener{
        public void onClick(View v) {
        }
    }

    /** classe interne pour gérer le clic sur le bouton buttonSupprimerUtilisateur. */
    private class buttonSupprimerUtilisateurClickListener implements View.OnClickListener{
        public void onClick(View v) {
        }
    }

    /** classe interne pour gérer le clic sur le bouton buttonDeconnecter. */
    private class buttonDeconnecterClickListener implements View.OnClickListener{
        public void onClick(View v) {
            finish();
        }
    }

} // FIN DE LA CLASSE

