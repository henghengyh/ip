package agnes.ui;

import java.util.List;

import agnes.Agnes;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Agnes agnes;
    private Stage stage;

    private Image userImage;
    private Image dukeImage;
    private ImageView displayPicture;

    /** Initialises the Main Window to be displayed by creating objects and showing welcome message */
    @FXML
    public void initialize() {
        try {
            // AI Recommended to use catch the error if the images are not present
            //
            //Missing Error Handling Image loading in MainWindow doesn't handle
            //NullPointerException if images are missing.
            //
            userImage = new Image(this.getClass().getResourceAsStream("/images/yiheng.jpg"));
            dukeImage = new Image(this.getClass().getResourceAsStream("/images/agnes.jpg"));
        } catch (NullPointerException e) {
            System.err.println("Error loading images: " + e.getMessage());
        }

        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        Ui ui = new Ui();
        dialogContainer.getChildren().add(
                DialogBox.getAgnesDialog(ui.getWelcomeMessage().get(0), dukeImage)
        );
    }

    /** Injects the Agnes instance and Stage reference */
    public void setAgnes(Agnes d, Stage stage) {
        agnes = d;
        this.stage = stage;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Agnes's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     * Closes the application after 2 seconds if the bye command is executed.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        List<String> responses = agnes.getResponse(input);
        dialogContainer.getChildren().add(
                DialogBox.getUserDialog(input, userImage));

        for (String msg : responses) {
            dialogContainer.getChildren().add(
                    DialogBox.getAgnesDialog(msg, dukeImage)
            );
        }
        userInput.clear();

        // AI Recommended to include this delay and check to close the application after the bye command is executed
        if (input.equals("bye")) {
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(event -> {
                if (stage != null) {
                    stage.close();
                }
            });
            delay.play();
        }
    }
}
