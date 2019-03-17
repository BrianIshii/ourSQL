import entity.Ingredient;
import entity.IngredientRecipe;
import entity.Instruction;
import entity.Recipe;
import exception.EntityNotFoundException;
import exception.ExecutorException;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import service.RecipeService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.util.Callback;

import java.io.IOException;

public class RecipeController extends BaseController {
    public static String FXML = "Recipe.fxml";
    public static Long recipeID = Long.valueOf(0);

    private RecipeService recipeService = RecipeService.getInstance();

    @FXML private Button backButton;
    @FXML private Button forwardButton;
    @FXML ImageView image;
    @FXML ListView<String> ingredientsView = new ListView<>();
    @FXML ListView<String> instructionsView = new ListView<>();
    @FXML Label recipeNameLabel;

    public RecipeController() {
        super(FXML);
    }

    @FXML
    public void initialize() {

        backButton.setDisable(!canPressBackButton());
        forwardButton.setDisable(!canPressForwardButton());

        System.out.println("Recipe id: " + recipeID);
        Recipe r = null;
        try{
            r = recipeService.searchById(recipeID);
            recipeNameLabel.setText((String)r.getFieldValue(Recipe.TITLE));
            String url = (String)r.getFieldValue(Recipe.URL);
            if (url != null &&
                    "" != url && !url.isEmpty()) {
                Image i = new Image((String)r.getFieldValue(Recipe.URL));
                image.setImage(i);
            }
        } catch(EntityNotFoundException enfe) {
            //TODO add failure behavior
        } catch (ExecutorException ee) {
            //TODO add failure behavior
        }

        for (IngredientRecipe ir : r.getIngredients()) {
            Ingredient i = ir.getIngredient();
            String ingredientInfo = (i.getFieldValue(Ingredient.NAME) + "/" + ir.getField("amount").getValue() + " " + ir.getField("unit").getValue());
            ingredientsView.getItems().add(ingredientInfo);
        }

        for (Instruction i : r.getInstructions()) {
            String instructionInfo = (i.getField("step").getValue() + ". " + i.getField("description").getValue());
            instructionsView.getItems().add(instructionInfo);
            System.out.println(instructionInfo);
        }

        ingredientsView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                System.out.println("selected");
            }
        });

        ingredientsView.setCellFactory(CheckBoxListCell.forListView(new Callback<String, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(String item) {
                BooleanProperty observable = new SimpleBooleanProperty();
                observable.addListener((obs, wasSelected, isNowSelected) ->
                        System.out.println("Check box for "+item+" changed from "+wasSelected+" to "+isNowSelected)
                );
                return observable ;
            }
        }));

        System.out.println(r);
    }

    public void homeButtonPressed(ActionEvent event) throws IOException {
        changeViewTo(HomeController.FXML);
    }
}
