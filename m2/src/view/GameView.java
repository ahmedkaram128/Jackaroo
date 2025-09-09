package view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import engine.Game;
import model.Colour;
import model.card.Card;
import model.card.standard.Standard;
import model.card.wild.Burner;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class GameView {
	
	private Label turns;
	private Rectangle firePit;
	private ImageView firePitImage;
	private Pane board;
	private ImageView deckImage;
	private double deckX = 50; 
	private double deckY = 50;
	
	private Button deleteCard;
	private Button playButton;
	private Button deselectAll;
	
	private StackPane boardStackPane;
	private Pane animationOverlay;
	private ArrayList<Region> hands;
	private ArrayList<Label> playerNames;
	private ArrayList<Colour> playerColours;
	
	final int cardWidth = 67;
	final int cardHeight = 100;
	final int CELL_SIZE = 24; 
    final int BOARD_SIZE = 26;
	
    private Button setting;
    
	public GameView(Game game){
		
		boardStackPane = new StackPane();
		board = new Pane();
		initializeBoardStackPane();
		
		turns = new Label();
		
		hands = new ArrayList<>(4);
		playerNames = new ArrayList<>(4);
		deleteCard = new Button("Discard Card");
		playButton = new Button("Play turn");
		deselectAll = new Button("Deselect All");
		animationOverlay = new Pane();
		
		playerColours = new ArrayList<>();
		for(int i = 0; i<4; i++){
			playerColours.add(game.getPlayers().get(i).getColour());
		}
		
		String[] names = {"cpu0", "Seif", "Ahmed", "Amr"};
		for(int i = 0; i<4 ; i++){	
			initializeValues(i, names[i]);
		}	
		
		setting = new Button("");
		setting.setTranslateY(-450);
		setting.setTranslateX(900);
		
	}
	public StackPane getView(){	
		StackPane view = new StackPane();
		
		int fixedWidth = 1920;
		int fixedHeight = 1080 ;
		
//		boardStackPane.setStyle("-fx-border-color: red");
		StackPane.setAlignment(boardStackPane, Pos.CENTER);
		String path = "res/view/view_game_background.png";
        Image image1;
		try {
			image1 = new Image(new FileInputStream(path));
			Image bgImage = image1;
			ImageView bgView = new ImageView(bgImage);
			bgView.setFitWidth(1950); 
			bgView.setFitHeight(1400); 
			bgView.setPreserveRatio(false);	
			
			ImageView icon = new ImageView(new Image(new FileInputStream("res/view/setting.png")));
			icon.setFitHeight(50);
			icon.setFitWidth(50);
			setting.setGraphic(icon);
			setting.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
			
			
			view.getChildren().addAll(bgView,boardStackPane,setting);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		StackPane.setAlignment(hands.get(1), Pos.CENTER_LEFT);    
		StackPane.setAlignment(hands.get(2), Pos.TOP_CENTER);     
		StackPane.setAlignment(hands.get(3), Pos.CENTER_RIGHT);   
		
		view.getChildren().addAll(
				hands.get(1),
				hands.get(2),
				hands.get(3)
			);
//		hands.get(1).setStyle("-fx-border-color: blue");
//		hands.get(2).setStyle("-fx-border-color: blue");
//		hands.get(3).setStyle("-fx-border-color: blue");
		StackPane.setAlignment(turns, Pos.BOTTOM_LEFT);
		view.getChildren().add(turns);
		
		HBox controls = new HBox(20, playButton, deleteCard, deselectAll);
		controls.setAlignment(Pos.CENTER);
		controls.setPadding(new Insets(20));
		controls.setStyle(
		    "-fx-background-color: #ffffffaa;" +  // translucent white background
		    "-fx-background-radius: 15px;" +
		    "-fx-border-color: #0078d7;" +
		    "-fx-border-radius: 15px;" +
		    "-fx-border-width: 2px;"
		);

		Button[] buttons = { playButton, deleteCard, deselectAll };
		for (Button btn : buttons) {
		    btn.setStyle(
		        "-fx-background-color: #28a745;" +
		        "-fx-text-fill: white;" +
		        "-fx-font-size: 14px;" +
		        "-fx-font-weight: bold;" +
		        "-fx-background-radius: 10px;" +
		        "-fx-padding: 10px 20px;"
		    );

		    btn.setOnMouseEntered(e -> btn.setStyle(
		        "-fx-background-color: #1e7e34;" +
		        "-fx-text-fill: white;" +
		        "-fx-font-size: 14px;" +
		        "-fx-font-weight: bold;" +
		        "-fx-background-radius: 10px;" +
		        "-fx-padding: 10px 20px;"
		    ));

		    btn.setOnMouseExited(e -> btn.setStyle(
		        "-fx-background-color: #28a745;" +
		        "-fx-text-fill: white;" +
		        "-fx-font-size: 14px;" +
		        "-fx-font-weight: bold;" +
		        "-fx-background-radius: 10px;" +
		        "-fx-padding: 10px 20px;"
		    ));
		}

		VBox human = new VBox(hands.get(0), controls);
		human.setPrefSize(hands.get(0).getPrefWidth(), hands.get(0).getPrefHeight() + 30);
		human.setMinSize(hands.get(0).getPrefWidth(), hands.get(0).getPrefHeight() + 30);
		human.setMaxSize(hands.get(0).getPrefWidth(), hands.get(0).getPrefHeight() + 30);
		StackPane.setAlignment(human, Pos.BOTTOM_CENTER);
		
		view.getChildren().add(human);
		view.setPrefSize(fixedWidth, fixedHeight);
		view.setMinSize(fixedWidth, fixedHeight);
		view.setMaxSize(fixedWidth, fixedHeight);
		view.setStyle("-fx-background-color: #ffffff");
		
		StackPane root = new StackPane(view); 
		root.setStyle("-fx-background-color: #000000");
		AnchorPane names = new AnchorPane();

		Label topLeftName = playerNames.get(2);
		topLeftName.setId("player2");
		AnchorPane.setTopAnchor(topLeftName, 100.0);
		AnchorPane.setLeftAnchor(topLeftName, 650.0);

		Label topRightName = playerNames.get(3);
		topRightName.setId("player3");
		AnchorPane.setTopAnchor(topRightName, 250.0);
		AnchorPane.setRightAnchor(topRightName, 100.0);

		Label bottomLeftName = playerNames.get(1);
		bottomLeftName.setId("player1");
		AnchorPane.setBottomAnchor(bottomLeftName, 250.0);
		AnchorPane.setLeftAnchor(bottomLeftName, 50.0);

		Label bottomRightName = playerNames.get(0);
		bottomRightName.setId("player0");
		AnchorPane.setBottomAnchor(bottomRightName, 100.0);
		AnchorPane.setRightAnchor(bottomRightName, 650.0);
		
		names.getChildren().addAll(topLeftName, topRightName, bottomLeftName, bottomRightName);
		names.setMouseTransparent(true);
		
		
		
		
		
		view.getChildren().add(names);
	    return root;
	}
	
	private void initializeBoardStackPane(){
		
		board.setPrefSize(BOARD_SIZE*CELL_SIZE, BOARD_SIZE*CELL_SIZE);
		board.setMinSize(BOARD_SIZE*CELL_SIZE, BOARD_SIZE*CELL_SIZE);
		board.setMaxSize(BOARD_SIZE*CELL_SIZE, BOARD_SIZE*CELL_SIZE);
		
		String path = "res/cards/card_empty.png";
		Image cardImage;
		try {
			cardImage = new Image(new FileInputStream(path));
			ImageView cardView = new ImageView(cardImage);
	        cardView.setFitWidth(cardWidth*1.5);
	        cardView.setPreserveRatio(true);
	        cardView.setSmooth(true);
			firePit = new Rectangle(cardWidth, cardHeight);
		    firePitImage = cardView;

		    firePit.setFill(Color.WHITE);
		    firePit.setStroke(Color.BLACK);
		    firePit.setStrokeWidth(5.0);

		    Group boardGroup = new Group(board);
		    deckImage = new ImageView(new Image("cards/card_back.png"));
			deckImage.setFitWidth(100);
			deckImage.setFitHeight(140);
			deckImage.setLayoutX(deckX);
			deckImage.setLayoutY(deckY);
			
		    boardStackPane.getChildren().addAll(new StackPane(firePit, firePitImage), boardGroup,deckImage);
		    boardStackPane.setPrefSize(BOARD_SIZE*CELL_SIZE, BOARD_SIZE*CELL_SIZE);
		    boardStackPane.setMinSize(BOARD_SIZE*CELL_SIZE, BOARD_SIZE*CELL_SIZE);
		    boardStackPane.setMaxSize(BOARD_SIZE*CELL_SIZE, BOARD_SIZE*CELL_SIZE);
		    
		    StackPane.setAlignment(firePit, Pos.CENTER);
		    StackPane.setAlignment(firePitImage, Pos.CENTER);
		    StackPane.setAlignment(deckImage, Pos.TOP_LEFT);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
		
	}
	
	private void initializeValues(int index, String name){
		
		Label playerName = new Label(name);
		playerNames.add(index, playerName);
		
		Region hand = createHand(index);
		hands.add(index, hand);
		if(hand instanceof HBox){
			hand.setPrefSize(BOARD_SIZE*CELL_SIZE, 200);
		    hand.setMinSize(BOARD_SIZE*CELL_SIZE, 200);
		    hand.setMaxSize(BOARD_SIZE*CELL_SIZE, 200);
		}
		if(hand instanceof VBox){
			hand.setPrefSize(200, BOARD_SIZE*CELL_SIZE);
			hand.setMinSize(200, BOARD_SIZE*CELL_SIZE);
			hand.setMaxSize(200, BOARD_SIZE*CELL_SIZE);
		}
	}
	
	
	

	
	private Region createHand(int index) {
	    Insets padding = new Insets(0);
	    if(index == 1 || index == 3){
	      
	    	VBox hand = new VBox(10); 
	        hand.setAlignment(Pos.CENTER);
	        hand.setPadding(padding);
	        return hand;
	    } 
	    else{
	    	HBox hand = new HBox(10); 
	        hand.setAlignment(Pos.CENTER);
	        hand.setPadding(padding);
	        return hand;  
	    }
	}
	
	public Circle addCell(Colour p,int col,int row, int index){
		Circle c = new Circle(CELL_SIZE *0.4);
		RadialGradient gradient = new RadialGradient(0, 0, 0, 0, 0, true, CycleMethod.NO_CYCLE);
		
		switch(p) {	
			case RED:
				gradient = new RadialGradient(0, 0, 0.3, 0.3, 1, true, CycleMethod.NO_CYCLE, new Stop(0, Color.rgb(255, 150, 150)),
		                new Stop(1, Color.RED.darker()));
		            break;
			case BLUE:
				gradient = new RadialGradient(0, 0, 0.3, 0.3, 1, true, CycleMethod.NO_CYCLE, new Stop(0, Color.rgb(150, 150, 255)),
		                new Stop(1, Color.BLUE.darker()));
		            break;
			case GREEN:
				gradient = new RadialGradient(0, 0, 0.3, 0.3, 1, true, CycleMethod.NO_CYCLE, new Stop(0, Color.rgb(150, 255, 150)),
		                new Stop(1, Color.GREEN.darker()));
		            break;
			case YELLOW:
				gradient = new RadialGradient(0, 0, 0.3, 0.3, 1, true, CycleMethod.NO_CYCLE, new Stop(0, Color.rgb(255, 255, 180)),
		                new Stop(1, Color.GOLD.darker()));
		            break;
		}
		
		DropShadow dropShadow = new DropShadow();
	    dropShadow.setRadius(4);
	    dropShadow.setOffsetX(2);
	    dropShadow.setOffsetY(2);
	    dropShadow.setColor(Color.gray(0, 0.6));
	    
	    c.setFill(gradient);
	    c.setEffect(dropShadow);
	    c.setLayoutX(col * CELL_SIZE);
	    c.setLayoutY(row * CELL_SIZE);
        board.getChildren().add(c);
        
        return c;
	}
	
	public void addBackGroundCell(int col, int row, int index){
		Circle c = new Circle(CELL_SIZE *0.4);
		RadialGradient gradient = new RadialGradient(0, 0, 0, 0, 0, true, CycleMethod.NO_CYCLE);
		if(index != 4){ // not on board
			switch(playerColours.get(index%5)) { // safeZone or HomeZone	
			case RED:
				gradient = new RadialGradient(0, 0, 0.3, 0.3, 1, true, CycleMethod.NO_CYCLE, new Stop(0, Color.rgb(100, 50, 50)),
		                new Stop(1, Color.DARKRED));
		            break;
			case BLUE:
				gradient = new RadialGradient(0, 0, 0.3, 0.3, 1, true, CycleMethod.NO_CYCLE, new Stop(0, Color.rgb(50, 50, 100)),
		                new Stop(1, Color.DARKBLUE));
		            break;
			case GREEN:
				gradient = new RadialGradient(0, 0, 0.3, 0.3, 1, true, CycleMethod.NO_CYCLE, new Stop(0, Color.rgb(50, 100, 50)),
		                new Stop(1, Color.DARKGREEN));
		            break;
			case YELLOW:
				gradient = new RadialGradient(0, 0, 0.3, 0.3, 1, true, CycleMethod.NO_CYCLE, new Stop(0, Color.rgb(120, 120, 60)),
		                new Stop(1, Color.GOLDENROD.darker()));
		            break;
			}
		}
		else{
			gradient = new RadialGradient(0, 0, 0.5, 0.5, 1, true, CycleMethod.NO_CYCLE, new Stop(0, Color.BLACK),
					new Stop(1, Color.rgb(50, 50, 50)));
		}
		
		DropShadow dropShadow = new DropShadow();
	    dropShadow.setRadius(4);
	    dropShadow.setOffsetX(2);
	    dropShadow.setOffsetY(2);
	    dropShadow.setColor(Color.gray(0, 0.6));
	    
	    c.setFill(gradient);
	    c.setEffect(dropShadow);
	    c.setLayoutX(col * CELL_SIZE);
	    c.setLayoutY(row * CELL_SIZE);
        board.getChildren().add(c);
	}
	
	public ImageView addCard(Card card,int index){
		
	    String link = null;
	    
	    if (card instanceof Standard) {
	    
	        Standard std = (Standard) card;
	        String suit = std.getSuit().toString().toLowerCase() + "s"; 
	        String rankPart;

	        int rank = std.getRank();
	        switch (rank) {
	            case 11: rankPart = "J"; break;
	            case 12: rankPart = "Q"; break;
	            case 13: rankPart = "K"; break;
	            case 1:  rankPart = "A"; break;
	            default:
	                rankPart = String.format("%02d", rank);
	        }

	        link = "card_" + suit + "_" + rankPart + ".png";
	    }
	     else if (card instanceof Burner) {
	        link = "card_burner.png"; 
	    }
	     else{
	    	 link = "card_saver.png"; 
	     }
	    ImageView cardView = null;
//	    ImageView otherView = null;
	    try {
	    	String path;
	    	if( index == 1 || index == 2 || index == 3)
		    	path = "res/cards/card_back.png";
	    	else
	    		path = "res/cards/" + link;
	        Image cardImage = new Image(new FileInputStream(path));
//	        Image otherImage = new Image(new FileInputStream("res/cards/" + link));
	        
	        cardView = new ImageView(cardImage);
	        cardView.setFitHeight(cardHeight);
	        cardView.setPreserveRatio(true);
	        cardView.setSmooth(true);
	        
//	        otherView = new ImageView(otherImage);
//	        otherView.setFitHeight(cardHeight);
//	        otherView.setPreserveRatio(true);
//	        otherView.setSmooth(true);
	    
		    if (index == 1 || index == 3) {
//		    	otherView.setRotate(90.0*index);
		        cardView.setRotate(90.0*index);
		        ((VBox) hands.get(index)).getChildren().add(cardView);
		    } else if(index == 0 || index == 2){
//		    	otherView.setRotate(90.0*index);
		    	cardView.setRotate(90.0*index);
		        ((HBox) hands.get(index)).getChildren().add(cardView);
		    } else {
		    	firePitImage.setImage(cardImage); 
		    	firePitImage.setFitHeight(cardHeight);
		    	firePitImage.setPreserveRatio(true);
		    	firePitImage.setSmooth(true);
		    }
	    } catch (FileNotFoundException e) {
	        System.err.println("Image file not found: " + link);
	    }
	    
    	return cardView;
	}
	
	public void animateCardToFirePit(ImageView cardView, int handIndex) {
	    Bounds cardBounds = cardView.localToScene(cardView.getBoundsInLocal());
	    Bounds firePitBounds = firePitImage.localToScene(firePitImage.getBoundsInLocal());

	    Point2D cardInOverlay = animationOverlay.sceneToLocal(cardBounds.getMinX(), cardBounds.getMinY());
	    Point2D firePitInOverlay = animationOverlay.sceneToLocal(firePitBounds.getMinX(), firePitBounds.getMinY());

	    ImageView flyingCard = new ImageView(cardView.getImage());
	    flyingCard.setLayoutX(cardInOverlay.getX());
	    flyingCard.setLayoutY(cardInOverlay.getY());
	    flyingCard.setRotate(cardView.getRotate());

	    animationOverlay.getChildren().add(flyingCard);

	    if (handIndex == 0 || handIndex == 2) {
	        ((HBox) (hands.get(handIndex))).getChildren().remove(cardView);
	    } else {
	        ((VBox) (hands.get(handIndex))).getChildren().remove(cardView);
	    }

	    // THROWING EFFECT 
	    double offsetX = firePitInOverlay.getX() - cardInOverlay.getX();
	    double offsetY = firePitInOverlay.getY() - cardInOverlay.getY();

	    // Add slight random offset to simulate hand flick
	    offsetX += Math.random() * 30 - 15;
	    offsetY += Math.random() * 30 - 15;

	    TranslateTransition move = new TranslateTransition(Duration.millis(400), flyingCard);
	    move.setByX(offsetX);
	    move.setByY(offsetY);
	    move.setInterpolator(Interpolator.EASE_OUT);

	    RotateTransition rotate = new RotateTransition(Duration.millis(400), flyingCard);
	    rotate.setByAngle(180); // full spin

	    ParallelTransition throwCard = new ParallelTransition(move, rotate);
	    throwCard.setOnFinished(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	            animationOverlay.getChildren().remove(flyingCard);
	        }
	    });

	    throwCard.play();
	}

	
	public Color getColor(Colour c){
		
		Color out = Color.BLACK;
		
		switch(c){
			case YELLOW:
				out = Color.YELLOW;
				break;
			case GREEN:
				out = Color.GREEN;
				break;
			case RED:
				out = Color.RED;
				break;
			case BLUE:
				out = Color.BLUE;
				break;
		}
		return out;
	}
	
	
	public void setLabelColors(Colour c0, Colour c1, Colour c2, Colour c3) {
	    List<Color> colors = Arrays.asList(getColor(c0), getColor(c1), getColor(c2), getColor(c3));

	    for (int i = 0; i < 4; i++) {
	        Label label = playerNames.get(i);
	        Color color = colors.get(i);

	        label.setBorder(new Border(new BorderStroke(
	            color, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3)
	        )));

	        label.setBackground(new Background(
	        	    new BackgroundFill(Color.WHITE, new CornerRadii(10), Insets.EMPTY)
	        	));


	        label.setPadding(new Insets(10));
	        label.setStyle(
	            "-fx-font-weight: bold;" +
	            "-fx-font-size: 16px;" +
	            "-fx-text-fill: " + toHex(color) + ";"
	        );
	    }
	}
	private String toHex(Color color) {
	    return String.format("#%02X%02X%02X",
	        (int) (color.getRed() * 255),
	        (int) (color.getGreen() * 255),
	        (int) (color.getBlue() * 255));
	}
	
	
	public Label getName(int index){
		return (Label)playerNames.get(index);
	}

	public Label getTurns() {
		return turns;
	}

	public Rectangle getFirePit() {
		return firePit;
	}

	public Pane getBoard() {
		return board;
	}

	public Button getDeleteCard() {
		return deleteCard;
	}

	public ArrayList<Region> getHands() {
		return hands;
	}

	public ArrayList<Label> getPlayerNames() {
		return playerNames;
	}

	public Button getPlayButton() {
		return playButton;
	}
	
	public Button getDeselectAllButton(){
		return deselectAll;
	}
	public Button getSetting() {
		return setting;
	}
	
	
	
	public void animateCardBackFromDeckToPlayer(Pane playerHand, double targetX, double targetY) {
	    Image backImage = new Image("images/cards/back.png");
	    ImageView flyingCard = new ImageView(backImage);
	    flyingCard.setFitWidth(80);
	    flyingCard.setFitHeight(112);
	    flyingCard.setLayoutX(deckX);
	    flyingCard.setLayoutY(deckY);
	    flyingCard.setRotate(0);

	    board.getChildren().add(flyingCard);

	    double offsetX = targetX - deckX + (Math.random() * 10 - 5);
	    double offsetY = targetY - deckY + (Math.random() * 10 - 5);

	    TranslateTransition move = new TranslateTransition(Duration.millis(300), flyingCard);
	    move.setByX(offsetX);
	    move.setByY(offsetY);
	    move.setInterpolator(Interpolator.EASE_OUT);

	    RotateTransition spin = new RotateTransition(Duration.millis(300), flyingCard);
	    spin.setByAngle(180);

	    ParallelTransition throwAnim = new ParallelTransition(move, spin);
	    throwAnim.setOnFinished(e -> board.getChildren().remove(flyingCard));

	    throwAnim.play();
	}





	    public void updateDeckVisibility(boolean isEmpty) {
	        if (deckImage != null) {
	            deckImage.setOpacity(isEmpty ? 0.3 : 1.0);
	        }
	    }
	    
	
}
