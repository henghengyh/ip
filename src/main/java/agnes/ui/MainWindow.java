package agnes.ui;

import java.util.List;

import agnes.Agnes;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
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

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/yiheng.jpg"));
    private Image dukeImage = new Image(this.getClass().getResourceAsStream("/images/agnes.jpg"));
    private ImageView displayPicture;

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        Ui ui = new Ui();
        dialogContainer.getChildren().add(
                DialogBox.getAgnesDialog(ui.getWelcomeMessage().get(0), dukeImage)
        );
    }

    /** Injects the Agnes instance */
    public void setAgnes(Agnes d) {
        agnes = d;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Agnes's reply and then appends them to
     * the dialog container. Clears the user input after processing.
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
    }
}
