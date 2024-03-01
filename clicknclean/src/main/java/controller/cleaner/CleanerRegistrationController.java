package controller.cleaner;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

import javafx.scene.control.ScrollPane;
import model.Address;
import view.Window;
import view.cleaner.CleanerWelcome;
import view.SceneId;
import tools.Db;

public class CleanerRegistrationController {
	public CleanerRegistrationController(
	    String name,
	    String surname,
	    String email,
	    String password,
	    String confirmpassword,
	    String phone,
	    LocalDate birthDate,
	    String houseNumber,
	    String label,
	    String postCode,
	    String city,
	    int km,
	    int hourlyRate,
	    String biography,
	    String motivation,
	    String experience,
	    String photo,
	    String idPhoto,
	    String photoLive,
	    Window window
	) {

		Db db = new Db();
		Address address;
		try {
			address = new Address(houseNumber, label, postCode, city);
		} catch (Exception e) {
			// TODO: Show the user that there was an error with the address
			return;
		}

		if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty() || confirmpassword.isEmpty() || phone.isEmpty() || birthDate == null || address == null || km == 0 || hourlyRate == 0 || biography.isEmpty() || motivation.isEmpty() || experience.isEmpty() || photo.isEmpty() || idPhoto.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Champs non remplis !");
			return;
		}
		if (!password.equals(confirmpassword)) {
			// Password doesn't match confirmpassword
			JOptionPane.showMessageDialog(null, "Le mot de passe n'est pas le même que la confirmation !");
			return;
		}

		// Idéalement ces valeurs pourraient être des constantes
		// pour que ce soit plus simple de les adapter à la db
		if (phone.length() != 10) {
			// Wrong phone number format
			JOptionPane.showMessageDialog(null, "Numéro de téléphone invalide !");
			return;
		}

		if (postCode.length() != 5) {
			// Incorrect postcode format
			JOptionPane.showMessageDialog(null, "Code postale invalide !");
			return;
		}

		if (biography.length() > 100) {
			// Biography too large
			JOptionPane.showMessageDialog(null, "Biography trop longue !");
			return;
		}

		if (motivation.length() > 250) {
			// Too much motivation :p
			JOptionPane.showMessageDialog(null, "Motivation trop longue !");
			return;
		}

		if (experience.length() > 250) {
			JOptionPane.showMessageDialog(null, "Expérience trop longue !");
			return;
		}

		if (isEmailAdress(email) == false) {
			//not good email format
			JOptionPane.showMessageDialog(null, "Mauvais format d'email !");
			return;
		}


		try {
			db.DAOAddCleaner(
			    name,
			    password,
			    surname,
			    email,
			    phone,
			    birthDate,
			    false,
			    address,
			    km,
			    hourlyRate,
			    biography,
			    idPhoto,
			    motivation,
			    experience,
			    false,
			    photo,
			    photoLive);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "L'inscription a échouée");
		}

		JOptionPane.showMessageDialog(null, "Inscription réussi ! Vous allez être dirigé vers votre page d'acceuil, vos accès sont limités en attente de confirmation de votre compte");
		window.setScene(new CleanerWelcome(new ScrollPane() ));
		// db.close();
	}

	public boolean isEmailAdress(String email) {
		Pattern p = Pattern
		            .compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$");
		Matcher m = p.matcher(email.toUpperCase());
		return m.matches();
	}


}