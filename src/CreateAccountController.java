
import Entity.User;
import Service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import java.util.regex.Pattern;

import java.io.IOException;

public class CreateAccountController {

    @FXML private TextField nameTextField;
    @FXML private TextField emailTextField;
    @FXML private TextField passwordTextField;
    @FXML private TextField confirmPasswordTextField;

    @FXML
    public void createAccountButtonPressed(ActionEvent event) throws IOException {
        // Reset textfields' style
        resetTextFieldsStyle();

        // Check if passwords are the same
        if (isValidInput()) {
            // Create user
            UserService userService = UserService.getInstance();

            User newUser= new User(nameTextField.getText(), emailTextField.getText(), passwordTextField.getText());
            userService.save(newUser);

            // Transition to home view
            Main.setUser(newUser);
            Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
            Main.getPrimaryStage().getScene().setRoot(root);
        } else {
            System.out.println("User create account failed");
        }
    }

    @FXML
    public void backButtonPressed(ActionEvent event) throws IOException {
        // Transition to main view
        Parent root = FXMLLoader.load(getClass().getResource("MainController.fxml"));
        Main.getPrimaryStage().getScene().setRoot(root);
    }

    private boolean isValidInput() {
        // Check empty fields
        if (nameTextField.getText().isEmpty() ||
            emailTextField.getText().isEmpty() ||
            passwordTextField.getText().isEmpty()) {
            System.out.println("Field(s) cannot be empty");

            if (nameTextField.getText().isEmpty()) alertTextField(nameTextField);
            if (emailTextField.getText().isEmpty()) alertTextField(emailTextField);
            if (passwordTextField.getText().isEmpty()) alertTextField(passwordTextField);

            return false;
        }

        // Check valid email
        if (!isValidEmail(emailTextField.getText())) {
            System.out.println("Email not valid");

            alertTextField(emailTextField);

            return false;
        }

        // Check password match
        if (passwordTextField.getText().compareTo(confirmPasswordTextField.getText()) != 0) {
            System.out.println("Passwords don't match");

            alertTextField(passwordTextField);
            alertTextField(confirmPasswordTextField);

            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    private void alertTextField(TextField textField) {
        textField.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");
    }

    private void resetTextFieldsStyle() {
        nameTextField.setStyle("");
        emailTextField.setStyle("");
        passwordTextField.setStyle("");
        confirmPasswordTextField.setStyle("");
    }
}
