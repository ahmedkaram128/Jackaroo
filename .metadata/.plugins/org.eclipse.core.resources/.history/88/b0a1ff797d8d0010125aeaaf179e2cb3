package application;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import engine.*;
import model.card.*;
import model.card.standard.Seven;
import model.player.Marble;
import model.player.Player;
import engine.board.BoardChange;
import engine.board.Cell;
import exception.GameException;
import exception.InvalidCardException;
import exception.InvalidMarbleException;
import view.AssetLoader;
import view.GameView;
import view.StartView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.transform.Scale;
import javafx.util.Duration;
import javafx.stage.Modality;
public class Main extends Application {
	private Game game;
	private Player humanPlayer;
	private GameView gameView;
	private StartView startView;
	private ArrayList<Point> trackCords;
	private MediaPlayer mediaPlayer;
	private ArrayList<Marble> prevTrack;
	private ArrayList<Marble> prevSafeZone;
	private ArrayList<Marble> prevHomeZone;
	private ArrayList<ArrayList<Card>> prevHands;
	private ArrayList<Marble> selectedMarbles;
	private ArrayList<Pane> hands = new ArrayList<>(); // UI hands for players
	private HashMap<Card, ImageView> cardViews;
	private HashMap<Marble, Circle> marbleViews;
	private final int CELL_SIZE = 24; 
	private int round;
	private final double BASE_WIDTH = 1920;
    private final double BASE_HEIGHT = 1080;
	private Stage ps ;
	@Override
	public void start(Stage primaryStage) {
		ps = primaryStage;
		playBackgroundMusic();
		startView = new StartView();
		
		StackPane root1 = startView.getView() ;
		//Scale scale = new Scale(1, 1, 0, 0);
		//root1.getTransforms().add(scale);
		
		Scene startScreen = new Scene(startView.getView(), 1920, 1080);
		TextField name = startView.getNameBox();
		
		startView.getSetting().setOnAction(event ->{
			displaySetting(primaryStage, startScreen);
		});
		
		
		
		startView.getStartGame().setOnAction(event -> {
			if(name.getText().equals("")){
				displayAlert("Name Error!!!", "Please enter a name!!!", "Try again");
				return;
			}

			try {
				game = new Game(name.getText());
				humanPlayer = game.getPlayers().get(0);
				
				trackCords = AssetLoader.loadTrack(12, 25);
				prevTrack = new ArrayList<>();
				prevSafeZone = new ArrayList<>();
				prevHomeZone = new ArrayList<>();
				prevHands = new ArrayList<>();
				selectedMarbles = new ArrayList<>();
				marbleViews = new HashMap<>();
				cardViews = new HashMap<>();
				
				gameView = new GameView(game);
				gameView.getName(0).setText(name.getText());
				
				
				StackPane root = gameView.getView();
				
				//Scale scale1 = new Scale(1, 1, 0, 0);
				//root.getTransforms().add(scale1);
				
				Scene gameScene = new Scene(root, 1920, 1200);
				
				
				
				initializeBoard();
				initializeHands();
				updateTurns();
				
				gameView.getSetting().setOnAction(ee ->{
					displaySetting(primaryStage, gameScene);
				});
				
				gameView.getDeleteCard().setOnAction(e -> {
					if(game.getActivePlayerColour() == humanPlayer.getColour() && humanPlayer.getSelectedCard()!= null){
						cloneBoard();
						game.endPlayerTurn();
						round++;
						round %= 4;
						updateHands();
						updateFireZone();
						updateTurns();
						playCpuWithDelay(0,3, primaryStage, startScreen);
					}
					
				});
				
				gameView.getPlayButton().setOnAction(e -> {
					try {
						if(game.getActivePlayerColour() == humanPlayer.getColour() &&game.canPlayTurn()){
							
							cloneBoard();
							
							game.playPlayerTurn();
							round++;
							round %= 4;
							
							if(humanPlayer.getMarbles().size() > prevHomeZone.size()){
								displayTrapAlert("Trap", "You fell in a trap cell");
							}
							updateBoard(() -> {
							    if (game.checkWin() == game.getActivePlayerColour()) {
							        displayWin("Game Over", game.getActivePlayerColour() + " wins", primaryStage, startScreen);
							    } else {
							        game.endPlayerTurn();
							        updateHands();
							        updateFireZone();
							        updateTurns();
							        playCpuWithDelay(0, 3, primaryStage, startScreen);
							    }
							});
						}
						else if(game.getActivePlayerColour() == humanPlayer.getColour()){
							humanPlayer.deselectAll();
							game.endPlayerTurn();
							round++;
							round %= 4;
							updateFireZone();
							updateTurns();
							playCpuWithDelay(0, 3, primaryStage, startScreen);
						}
						
					} catch (Exception e1) {
						displayAlert("Invalid action", e1.getMessage(), "Try again");
					}
				});
				
				gameView.getDeselectAllButton().setOnAction(e1 -> {
					if (game.getActivePlayerColour() == humanPlayer.getColour()) {
						humanPlayer.deselectAll();
						for(int i = 0; i<selectedMarbles.size(); i++){
							Circle marbleView = marbleViews.get(selectedMarbles.remove(i));
							marbleView.setStroke(null);
						}
						
					}
				});
				
				gameScene.setOnKeyPressed(e1 -> {
				    if (e1.getCode() == KeyCode.W) {
				        try {
				            cloneBoard();
				            game.fieldMarble();

				            updateBoard(() -> {}); 

				        } catch (Exception e2) {
				            e2.printStackTrace();
				        }
				    }
				});
				
				gameScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				/*Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
			    primaryStage.setX(screenBounds.getMinX());
			    primaryStage.setY(screenBounds.getMinY());
			    primaryStage.setWidth(screenBounds.getWidth());
			    primaryStage.setHeight(screenBounds.getHeight());*/
			    //
				primaryStage.setScene(gameScene);
			
				//gameScene.widthProperty().addListener((obs, oldVal, newVal) -> scaleGame(primaryStage, scale1));
				//gameScene.heightProperty().addListener((obs, oldVal, newVal) -> scaleGame(primaryStage, scale1));
				
				//primaryStage.setResizable(false);
				
				primaryStage.show();
				
				//scaleGame(primaryStage, scale1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		});	
		
		name.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                startView.getStartGame().fire(); // works when entering {enter}
            }
        });
		startScreen.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		// to make the stage size same as the size of the screen 
		/*Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
	    primaryStage.setX(screenBounds.getMinX());
	    primaryStage.setY(screenBounds.getMinY());
	    primaryStage.setWidth(screenBounds.getWidth());
	    primaryStage.setHeight(screenBounds.getHeight());*/
		// 
		primaryStage.setScene(startScreen);
		startScreen.getRoot().requestFocus();
		
		//startScreen.widthProperty().addListener((obs, oldVal, newVal) -> scaleGame(primaryStage, scale));
		//startScreen.heightProperty().addListener((obs, oldVal, newVal) -> scaleGame(primaryStage, scale));
		//primaryStage.setResizable(false);
		
		
		primaryStage.setTitle("Jakaroo");
		try{
			primaryStage.getIcons().add(new Image(new FileInputStream("res/view/logo.png")));
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		primaryStage.show();
		
		//scaleGame(primaryStage, scale);
	}
	
	
	private void scaleGame(Stage stage, Scale scale) {
        double scaleX = stage.getWidth() / BASE_WIDTH;
        double scaleY = stage.getHeight() / BASE_HEIGHT;
        double scaleFactor = Math.min(scaleX, scaleY); // Keep aspect ratio
        scale.setX(scaleX);
        scale.setY(scaleY);
    }
	
	private void initializeBoard(){
		Point cords;
		for(int i = 0; i<100; i++){
			cords = trackCords.get(i);
			gameView.addBackGroundCell(cords.x, cords.y, 4);
		}
		
		for(int i = 0; i<4; i++){
			for(int j = 0; j<4; j++){
				cords = getSafeZoneCords(i).get(j);
				gameView.addBackGroundCell(cords.x, cords.y, i+5);
			}
		}
		
		
		for(int i = 0; i<4; i++){
			for(int j = 0; j<4; j++){
				cords = getHomeZoneCords(i).get(j);
				Marble thisMarble = game.getPlayers().get(i).getMarbles().get(j);
				
				gameView.addBackGroundCell(cords.x, cords.y, i);
				Circle marbleView = gameView.addCell(thisMarble.getColour(), cords.x, cords.y, i);
				
				marbleView.setOnMouseClicked(e -> {
					try {
						humanPlayer.selectMarble(thisMarble);
						if(!selectedMarbles.contains(thisMarble)){
							selectedMarbles.add(thisMarble);
							marbleView.setStroke(Color.BROWN);
					        marbleView.setStrokeWidth(4);
					        marbleView.setStrokeType(StrokeType.INSIDE);
						}
						
						
					} catch (InvalidMarbleException exc) {
						displayAlert("Invalid Marble Selected", "stop", "try again");
					}
				});
				
				marbleViews.put(thisMarble, marbleView);
			}
		}
		
	}
	
	public void initializeHands(){
		ArrayList<Player> players = game.getPlayers();
		ArrayList<Card> hand;
		Region test;
		HBox testH;
		VBox testV;
		ArrayList<Region> hands = gameView.getHands();
		for(int i = 0; i<4; i++){
			test = hands.get(i);
			if(test instanceof HBox){
				testH = (HBox)(test);
				testH.getChildren().clear();
			}
			else{
				testV = (VBox)(test);
				testV.getChildren().clear();
			}
			
			hand = players.get(i).getHand();
			for(int j = 0; j< hand.size(); j++){
				Card card = hand.get(j);
				if(card == null) return;
				ImageView cardView = gameView.addCard(card, i);
				cardViews.put(card, cardView);
//				r.getChildren().forEach(child -> child.setMouseTransparent(true));
				if(i>0) continue;
				cardView.setOnMouseClicked(e -> {
					try {
						if(card instanceof Seven)
							displayEditSplitDistance("Split Distance", "Please enter the split distance: ");
						humanPlayer.selectCard(card);
						System.out.println("Selected " + card.getName());
					} catch (InvalidCardException e1) {
						displayAlert("invalid selection", "incorrect", "wait your turn");
					}
				});
			}
		}
	}
	
	public void updateHands(){ // used when new cards from deck are distributed
		Stack<ImageView> views = new Stack<>();
		Stack<Integer> indxs = new Stack<>() ;
		ArrayList<Card> hand;
		ArrayList<Card> thisPrevHand;
		
		if(round == 0 && game.getActivePlayerIndex() ==0){
			initializeHands();
			return;
		}
		
		for(int i = 0; i<4; i++){
			hand = game.getPlayers().get(i).getHand();
			thisPrevHand = prevHands.get(i);
			if(hand.size()< thisPrevHand.size()){ // either card burned or finished playing
				for(int j = 0; j< thisPrevHand.size(); j++){
					if(!hand.contains(thisPrevHand.get(j))){
						views.push(cardViews.get(thisPrevHand.get(j)));
						indxs.push(i);
						break;
					}
				}
			}
		}
		
		while(!views.isEmpty()){
			gameView.animateCardToFirePit(views.pop(),indxs.pop());
		}
		
	}
	public void animateDealingCoversOnly() {
		ArrayList<Card> hand;
		for (int i = 0; i < 4; i++) { // 4 players
        Pane playerHand = (Pane)hands.get(i);
        Bounds bounds = playerHand.localToScene(playerHand.getBoundsInLocal());
        double targetX = bounds.getMinX();
        double targetY = bounds.getMinY();

        for (int j = 0; j < 4; j++) { // 4 cards each
            int delay = (i * 4 + j) * 100; // 100ms between each throw

            PauseTransition pause = new PauseTransition(Duration.millis(delay));
            final int ii = i; final int jj = j;
            pause.setOnFinished(e -> gameView.animateCardBackFromDeckToPlayer(playerHand, targetX, targetY));
            pause.play();
        }
    }
}


	
	private void updateBoard(Runnable afterAnimation){
		int newIndex;
		Marble oldMarble;
		Marble newMarble;
		ArrayList<Cell> track = game.getBoard().getTrack();
		ArrayList<Cell> safeZone = game.getBoard().getSafeZones().get(game.getActivePlayerIndex()).getCells();
		Stack<BoardChange> changes = new Stack<>();
		
		for(int i = 0; i< 100; i++){
			oldMarble = prevTrack.get(i);
			newMarble = track.get(i).getMarble();
			
			if(oldMarble != null && newMarble != oldMarble){
				newIndex = indexOfMarble(track, oldMarble);
				
				if(newIndex!= -1){ // changed position, still in track
					changes.push(new BoardChange(oldMarble, i, 4, newIndex, 4));
				}
				else if(oldMarble.getColour() == game.getActivePlayerColour()){
						// check safeZone if marble same colour as active player
						newIndex = indexOfMarble(safeZone, oldMarble);
						if(newIndex != -1){// in safeZone
							changes.push(new BoardChange(oldMarble, i, 4, newIndex, 5 + game.getActivePlayerIndex()));
						}
						else{// activePlayer marble returned to homeZone 
							newIndex = 4 - game.getPlayers().get(game.getActivePlayerIndex()).getMarbles().size();
							changes.push(new BoardChange(oldMarble, i, 4, newIndex, game.getActivePlayerIndex()));
						}
					}
				else{ // marble returned to homeZone
					int playerIndex = game.getPlayerIndex(oldMarble.getColour());
					newIndex = 4- game.getPlayers().get(playerIndex).getMarbles().size();
					changes.push(new BoardChange(oldMarble, i, 4, newIndex, playerIndex));
				}		
					
			}
			if(newMarble != null && !prevTrack.contains(newMarble)){ // newly fielded marble
				int playerIndex = game.getPlayerIndex(newMarble.getColour());
				int oldIndex = 4 - prevHomeZone.size();
				changes.push(new BoardChange(newMarble, oldIndex, playerIndex, i, 4));
				System.out.println(prevHomeZone.size());
			}
			
		}
		for(int i = 0; i<4; i++){ // marble already in safeZone moves in the safeZone
			oldMarble = prevSafeZone.get(i);
			newMarble = safeZone.get(i).getMarble();
			if(oldMarble != null && oldMarble != newMarble){
				int playerIndex = game.getActivePlayerIndex();
				newIndex = indexOfMarble(safeZone, oldMarble);
				changes.push(new BoardChange(oldMarble, i, 5 + playerIndex, newIndex, 5 + playerIndex));
			}
		}
		
		for(int i = 0; i<selectedMarbles.size(); i++){
			Circle marbleView = marbleViews.get(selectedMarbles.remove(i));
			marbleView.setStroke(null);
		}
		
		animate(changes, afterAnimation);
		
	}
	
	private int indexOfMarble(ArrayList<Cell> path, Marble marble){
		for(int i = 0; i< path.size(); i++){
			if(path.get(i).getMarble() == marble)
				return i;
			
		}
		return -1;
	}
	
	private void animate(Stack<BoardChange> changes, Runnable onFinished) {
	    SequentialTransition sequence = new SequentialTransition();

	    while (!changes.isEmpty()) {
	        BoardChange change = changes.pop();
	        Circle marbleView = marbleViews.get(change.marble);
	        
	        Point from = getCoords(change.from, change.init);
	        Point to = getCoords(change.to, change.target);
	        
	        marbleView.toFront();
	        marbleView.setLayoutX(from.getX()*CELL_SIZE);
	        marbleView.setLayoutY(from.getY()*CELL_SIZE);
	        marbleView.setTranslateX(0);
	        marbleView.setTranslateY(0);

	        double dx = (to.x - from.x)*CELL_SIZE;
	        double dy = (to.y - from.y)*CELL_SIZE;

	        TranslateTransition move = new TranslateTransition(Duration.millis(500), marbleView);
	        move.setToX(dx);
	        move.setToY(dy);
	        move.setOnFinished(e -> {
	            marbleView.setTranslateX(0);
	            marbleView.setTranslateY(0);
	            marbleView.setLayoutX(to.getX()*CELL_SIZE);
	            marbleView.setLayoutY(to.getY()*CELL_SIZE);
	        });

	        sequence.getChildren().add(move);
	    }

	    sequence.setOnFinished(e -> {
	        if (onFinished != null) onFinished.run();
	    });

	    sequence.play();
	}
	
	private Point getCoords(int zone, int index) {
		
	    if (zone < 4) // homeZone
	        return getHomeZoneCords(zone).get(index);
	    else if (zone > 4) // safeZone
	        return getSafeZoneCords(zone % 5).get(index);
	    else // track
	        return trackCords.get(index); 
	}
	
	private ArrayList<Point> getHomeZoneCords(int index){
		ArrayList<Point> cords = new ArrayList<>();
		switch(index){
			case 0:
				cords.add(new Point(17,21)); cords.add(new Point(18,21));
				cords.add(new Point(17,22)); cords.add(new Point(18,22));
				break;
			case 1:
				cords.add(new Point(5,16)); cords.add(new Point(5,17));
				cords.add(new Point(4,16)); cords.add(new Point(4,17));
				break;
			case 2:
				cords.add(new Point(11,5)); cords.add(new Point(10,5));
				cords.add(new Point(11,4)); cords.add(new Point(10,4));
				break;
			case 3:
				cords.add(new Point(23,10)); cords.add(new Point(23,9));
				cords.add(new Point(24,10)); cords.add(new Point(24,9));
				break;
		}
		return cords;
	}
	
	private void updateFireZone(){
		ArrayList<Card> firePit = game.getFirePit();
		if(firePit.size() ==0)
			return;
		Card card = firePit.get(firePit.size() - 1);
		if(card == null)
			return;
		gameView.addCard(card, 5);
		
	}
	
	private void updateTurns(){
		int index = game.getActivePlayerIndex();
		gameView.getTurns().setText("Current player: "+ gameView.getPlayerNames().get(index).getText() 
				+ ", Next player: " + gameView.getPlayerNames().get((index + 1)%4).getText());
	}
	
	private ArrayList<Point> getSafeZoneCords(int safeZoneIndex){
		ArrayList<Point> cords = new ArrayList<>();
		switch(safeZoneIndex){
		
			case 0:
				cords.add(new Point(14, 24)); cords.add(new Point(14, 23));
				cords.add(new Point(14, 22)); cords.add(new Point(14, 21));
				break;
			case 1:
				cords.add(new Point(2, 13)); cords.add(new Point(3, 13));
				cords.add(new Point(4, 13)); cords.add(new Point(5, 13));
				break;
			case 2:
				cords.add(new Point(14, 2)); cords.add(new Point(14, 3)); 
				cords.add(new Point(14, 4)); cords.add(new Point(14, 5));
				break;
			case 3:
				cords.add(new Point(26, 13)); cords.add(new Point(25, 13)); 
				cords.add(new Point(24, 13)); cords.add(new Point(23, 13));
				break;
		}
		return cords;
	}
	
	private void playCpuWithDelay(int index, int max, Stage primaryStage, Scene startScreen) {
	    if (index >= max)
	        return;

	    PauseTransition pause = new PauseTransition(Duration.seconds(3.0));
	    pause.setOnFinished(event -> {
	        Player thisPlayer = game.getPlayers().get(game.getActivePlayerIndex());
	        int homeZoneSize = thisPlayer.getMarbles().size();

	        try {
	            cloneBoard();

	            if (game.canPlayTurn()) {
	                game.playPlayerTurn();
	            }

	            if (thisPlayer.getMarbles().size() > homeZoneSize) {
	                displayTrapAlert("Trap", "CPU" + index + " fell in a trap");
	            }

	            updateBoard(() -> {
	                if (game.checkWin() == game.getActivePlayerColour()) {
	                    displayWin("Game Over", game.getActivePlayerColour() + " wins", primaryStage, startScreen);
	                } else {
	                    game.endPlayerTurn();
	                    updateHands();
	                    updateFireZone();
	                    updateTurns();
	                    playCpuWithDelay(index + 1, max, primaryStage, startScreen);
	                }
	            });

	        } catch (GameException e) {
	            e.printStackTrace();
	        }
	    });

	    pause.play();
	}
	
	@SuppressWarnings("unchecked")
	private void cloneBoard(){
		ArrayList<Cell> track = game.getBoard().getTrack();
		ArrayList<Cell> safeZone = game.getBoard().getSafeZones().get(game.getActivePlayerIndex()).getCells();
		ArrayList<Marble> homeZone = game.getPlayers().get(game.getActivePlayerIndex()).getMarbles();
		Card selectedCard = game.getPlayers().get(game.getActivePlayerIndex()).getSelectedCard();
		
		prevTrack.clear();
		prevSafeZone.clear();
		prevHomeZone.clear();
		prevHands.clear();
		
		for(int i =0; i< 100; i++){
			prevTrack.add(track.get(i).getMarble());
		}
		
		for(int i = 0; i<4; i++){
			 prevSafeZone.add(safeZone.get(i).getMarble());
		}
		
		prevHomeZone = (ArrayList<Marble>)(homeZone.clone());
		
		for(int i = 0; i<4; i++){
			prevHands.add((ArrayList<Card>)(game.getPlayers().get(i).getHand().clone()));
		}
		
	}
	
	 /* private void showVibratingPopup() {
	    // Create the popup stage
	    Stage popupStage = new Stage();
	    popupStage.initStyle(StageStyle.TRANSPARENT); // Transparent window

	    // Load your cross image (Make sure the image path is correct)
	    ImageView crossImage;
		try {
			crossImage = new ImageView(new Image(new FileInputStream("res/view/red cross.png")));
			crossImage.setFitWidth(100);  // Optional: Resize
		    crossImage.setFitHeight(100);

		    StackPane root = new StackPane(crossImage);
		    root.setStyle("-fx-background-color: transparent;");

		    Scene scene = new Scene(root, 120, 120);
		    scene.setFill(Color.TRANSPARENT); // Transparent scene

		    popupStage.setScene(scene);
		    popupStage.show();

		    // Create the vibration effect
		    Timeline vibration = new Timeline(
		            new KeyFrame(Duration.millis(50), new KeyValue(crossImage.translateXProperty(), -5)),
		            new KeyFrame(Duration.millis(100), new KeyValue(crossImage.translateXProperty(), 5)),
		            new KeyFrame(Duration.millis(150), new KeyValue(crossImage.translateXProperty(), -5)),
		            new KeyFrame(Duration.millis(200), new KeyValue(crossImage.translateXProperty(), 5)),
		            new KeyFrame(Duration.millis(250), new KeyValue(crossImage.translateXProperty(), 0))
		    );
		    vibration.setCycleCount(3); 
		    vibration.setOnFinished(e -> popupStage.close());
		 
		    vibration.play();
		} catch (FileNotFoundException e1) {

			e1.printStackTrace();
		} 
	    
	} */
	
	private void displayAlert(String title, String message, String exitMessage) {
	    Stage alertStage = new Stage();
	    alertStage.setTitle(title);
	    alertStage.setResizable(false);

	    Label label = new Label(message);
	    label.setWrapText(true);
	    label.setStyle(
	        "-fx-font-size: 18px;" +
	        "-fx-text-fill: #856404;" + // dark yellow-brown
	        "-fx-font-weight: bold;"
	    );
	    label.setAlignment(Pos.CENTER);

	    Button closeButton = new Button(exitMessage);
	    closeButton.setStyle(
	        "-fx-background-color: #ffc107;" + // amber
	        "-fx-text-fill: black;" +
	        "-fx-font-size: 14px;" +
	        "-fx-font-weight: bold;" +
	        "-fx-background-radius: 10px;" +
	        "-fx-padding: 8px 16px;"
	    );
	    closeButton.setOnMouseEntered(e -> closeButton.setStyle(
	        "-fx-background-color: #e0a800;" + // darker amber on hover
	        "-fx-text-fill: black;" +
	        "-fx-font-size: 14px;" +
	        "-fx-font-weight: bold;" +
	        "-fx-background-radius: 10px;" +
	        "-fx-padding: 8px 16px;"
	    ));
	    closeButton.setOnMouseExited(e -> closeButton.setStyle(
	        "-fx-background-color: #ffc107;" +
	        "-fx-text-fill: black;" +
	        "-fx-font-size: 14px;" +
	        "-fx-font-weight: bold;" +
	        "-fx-background-radius: 10px;" +
	        "-fx-padding: 8px 16px;"
	    ));

	    closeButton.setOnAction(event -> alertStage.close());

	    VBox content = new VBox(20, label, closeButton);
	    content.setAlignment(Pos.CENTER);
	    content.setStyle(
	        "-fx-background-color: #fff3cd;" + // light yellow
	        "-fx-border-color: #ffeeba;" +
	        "-fx-border-width: 3px;" +
	        "-fx-border-radius: 15px;" +
	        "-fx-background-radius: 15px;" +
	        "-fx-padding: 30px;"
	    );

	    Scene scene = new Scene(content, 450, 180);
	    alertStage.setScene(scene);
	    alertStage.show();
	    //showVibratingPopup();
	    
	}
	
	private void displayTrapAlert(String title, String message) {
	    Stage alertStage = new Stage();
	    alertStage.setTitle(title);
	    alertStage.setResizable(false);

	    Label label = new Label(message);
	    label.setWrapText(true);
	    label.setAlignment(Pos.CENTER);
	    label.setStyle(
	        "-fx-font-size: 20px;" +
	        "-fx-font-weight: bold;" +
	        "-fx-text-fill: #721c24;"
	    );

	    VBox content = new VBox(label);
	    content.setAlignment(Pos.CENTER);
	    content.setStyle(
	        "-fx-background-color: #f8d7da;" +  // light red/pink
	        "-fx-border-color: #f5c6cb;" +
	        "-fx-border-width: 3px;" +
	        "-fx-border-radius: 15px;" +
	        "-fx-background-radius: 15px;" +
	        "-fx-padding: 30px;"
	    );

	    Scene scene = new Scene(content, 450, 180);
	    alertStage.setScene(scene);
	    alertStage.show();

	    PauseTransition delay = new PauseTransition(Duration.seconds(2));
	    delay.setOnFinished(e -> alertStage.close());
	    delay.play();
	}
	
	private void displayEditSplitDistance(String title, String message) {
	    Stage alertStage = new Stage();
	    alertStage.setTitle(title);
	    alertStage.setResizable(false);

	    Label label = new Label(message);
	    label.setStyle(
	        "-fx-font-size: 18px;" +
	        "-fx-font-weight: bold;" +
	        "-fx-text-fill: #0c5460;"
	    );

	    TextField splitDistance = new TextField();
	    splitDistance.setPromptText("Enter split distance...");
	    splitDistance.setStyle(
	        "-fx-background-radius: 6px;" +
	        "-fx-padding: 6px;" +
	        "-fx-font-size: 14px;"
	    );

	    Button submitButton = new Button("Enter");
	    submitButton.setStyle(
	        "-fx-background-color: #17a2b8;" +  // blue-teal
	        "-fx-text-fill: white;" +
	        "-fx-font-weight: bold;" +
	        "-fx-background-radius: 10px;" +
	        "-fx-padding: 8px 16px;"
	    );
	    submitButton.setOnMouseEntered(e -> submitButton.setStyle(
	        "-fx-background-color: #117a8b;" +
	        "-fx-text-fill: white;" +
	        "-fx-font-weight: bold;" +
	        "-fx-background-radius: 10px;" +
	        "-fx-padding: 8px 16px;"
	    ));
	    submitButton.setOnMouseExited(e -> submitButton.setStyle(
	        "-fx-background-color: #17a2b8;" +
	        "-fx-text-fill: white;" +
	        "-fx-font-weight: bold;" +
	        "-fx-background-radius: 10px;" +
	        "-fx-padding: 8px 16px;"
	    ));

	    submitButton.setOnAction(event -> {
	        if (!splitDistance.getText().trim().isEmpty()) {
	            try {
	                game.editSplitDistance(Integer.parseInt(splitDistance.getText().trim()));
	                alertStage.close();
	            } catch (Exception e1) {
	                displayAlert("Split Distance Error", e1.getMessage(), "Try again");
	            }
	        }
	    });

	    VBox view = new VBox(15, label, splitDistance, submitButton);
	    view.setAlignment(Pos.CENTER);
	    view.setStyle(
	        "-fx-background-color: #d1ecf1;" +  // light blue
	        "-fx-border-color: #bee5eb;" +
	        "-fx-border-width: 3px;" +
	        "-fx-border-radius: 15px;" +
	        "-fx-background-radius: 15px;" +
	        "-fx-padding: 30px;"
	    );

	    Scene scene = new Scene(view, 480, 220);
	    alertStage.setScene(scene);
	    alertStage.show();
	}
	
	private void displayWin(String title, String message, Stage primaryStage, Scene startScreen) {
	    Stage alertStage = new Stage();
	    alertStage.setTitle(title);
	    alertStage.setResizable(false);

	    // Winner message label
	    Label label = new Label(message);
	    label.setStyle(
	        "-fx-font-size: 32px;" +
	        "-fx-font-weight: bold;" +
	        "-fx-text-fill: #28a745;" +
	        "-fx-alignment: center;"
	    );
	    label.setWrapText(true);
	    label.setAlignment(Pos.CENTER);

	    // "Play Again" button
	    Button tryAgainButton = new Button("🔁 Play Again");
	    tryAgainButton.setStyle(
	        "-fx-background-color: #0078d7;" +
	        "-fx-text-fill: white;" +
	        "-fx-font-size: 16px;" +
	        "-fx-font-weight: bold;" +
	        "-fx-padding: 10px 20px;" +
	        "-fx-background-radius: 8px;"
	    );
	    tryAgainButton.setOnMouseEntered(e -> tryAgainButton.setStyle(
	        "-fx-background-color: #005a9e;" +
	        "-fx-text-fill: white;" +
	        "-fx-font-size: 16px;" +
	        "-fx-font-weight: bold;" +
	        "-fx-padding: 10px 20px;" +
	        "-fx-background-radius: 8px;"
	    ));
	    tryAgainButton.setOnMouseExited(e -> tryAgainButton.setStyle(
	        "-fx-background-color: #0078d7;" +
	        "-fx-text-fill: white;" +
	        "-fx-font-size: 16px;" +
	        "-fx-font-weight: bold;" +
	        "-fx-padding: 10px 20px;" +
	        "-fx-background-radius: 8px;"
	    ));

	    tryAgainButton.setOnAction(event -> {
	        primaryStage.setScene(startScreen);
	        alertStage.close();
	    });

	    // Layout container
	    VBox contentBox = new VBox(30, label, tryAgainButton);
	    contentBox.setAlignment(Pos.CENTER);
	    contentBox.setStyle(
	        "-fx-background-color: #ffffff;" +
	        "-fx-border-color: #28a745;" +
	        "-fx-border-width: 3px;" +
	        "-fx-border-radius: 15px;" +
	        "-fx-background-radius: 15px;" +
	        "-fx-padding: 40px;"
	    );

	    Scene scene = new Scene(contentBox, 500, 250);
	    alertStage.setScene(scene);
	    alertStage.show();
	}
	private void displaySetting(Stage primaryStage, Scene startScreen) {
        
        try {	
        	Stage alertStage = new Stage();
            alertStage.setTitle("Setting");
            
            try{
            	alertStage.getIcons().add(new Image(new FileInputStream("res/view/setting.png")));
    		}catch (FileNotFoundException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
           
            Button mute = new Button("");
            Button info = new Button("");
            
            mute.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
            info.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

			ImageView speakerImageView = new ImageView(new Image(new FileInputStream("res/view/speaker.png")));
			speakerImageView.setFitHeight(30);
			speakerImageView.setFitWidth(30);
			mute.setGraphic(speakerImageView);
			
			if (!mediaPlayer.isMute()) {
        		try {
        			speakerImageView.setImage(new Image(new FileInputStream("res/view/speaker.png")));
				} catch (Exception e) {
					e.printStackTrace();
				}
        	}else {
        		try {
        			speakerImageView.setImage(new Image(new FileInputStream("res/view/mute speaker.png")));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
			
			ImageView infoImagaView = new ImageView(new Image(new FileInputStream("res/view/info.png")));
			infoImagaView.setFitHeight(30);
			infoImagaView.setFitWidth(30);
			info.setGraphic(infoImagaView);
			
			HBox h = new HBox();
			h.getChildren().add(info);
			h.getChildren().add(mute);
			h.setAlignment(Pos.CENTER);
			
			BorderPane pane = new BorderPane();
	        pane.setCenter(h);
	     
	        
	        info.setOnAction(e -> {
	            
	            Stage infoStage = new Stage();
	            infoStage.setTitle("Jakaroo - Game Rules");

	            // Optional: Make it modal so the player focuses on it
	            infoStage.initModality(Modality.WINDOW_MODAL);
	            infoStage.initOwner(primaryStage); // Replace with your main stage variable

	            
	            String rules = 
	            	    "Jakaroo Game Rules\n\n" +

	            	    "Objective:\n" +
	            	    "Move all four of your marbles from your Home Zone to your Safe Zone before your opponents.\n" +
	            	    "You need to field your marbles using an Ace or King, then move them around the track into your Safe Zone.\n\n" +

	            	    "Game Setup:\n" +
	            	    "- Single player versus 3 CPU players.\n" +
	            	    "- Each player has 4 marbles with unique colors.\n" +
	            	    "- Each player has a Home Zone, Base Cell, and Safe Zone.\n" +
	            	    "- The board has a 100-cell track and 8 trap cells that move.\n\n" +

	            	    "Card Mechanics:\n" +
	            	    "- 102-card deck with 15 card types including wild cards.\n" +
	            	    "- Each player gets 4 cards per round.\n" +
	            	    "- Cards control marble movement, swapping, burning, saving, and skipping turns.\n\n" +

	            	    "Key Rules:\n" +
	            	    "• Move marbles based on card rank (number of steps).\n" +
	            	    "• You cannot move through your own marbles or more than one blocked marble.\n" +
	            	    "• Marbles can only enter the Safe Zone with an exact move count.\n" +
	            	    "• Marbles in the Safe Zone and Home Zone are protected from attacks.\n" +
	            	    "• Trap cells destroy marbles and change position after each activation.\n\n" +

	            	    "Special Actions:\n" +
	            	    "- Swapping: Use Jack cards to swap marbles with opponents.\n" +
	            	    "- Burning: Use Burner cards to remove opponent marbles from the track.\n" +
	            	    "- Saving: Use Saver cards to directly send your marble to a Safe cell.\n" +
	            	    "- Skipping: Use Ten or Queen cards to discard an opponent’s card and skip their turn.\n\n" +

	            	    "Invalid Moves:\n" +
	            	    "- Moving through or onto your own marbles.\n" +
	            	    "- Entering the Safe Zone if the entry cell is blocked.\n" +
	            	    "- Moving into a Base cell occupied by another player’s marble.\n\n" +

	            	    "Winning the Game:\n" +
	            	    "The first player to move all four marbles into their Safe Zone wins.\n\n" +

	            	    "Have fun and play smart!";



	            // Display the rules in a label or text area
	            TextArea infoText = new TextArea(rules);
	            infoText.setWrapText(true);
	            infoText.setEditable(false);
	            infoText.setStyle("-fx-font-size: 20;");
	            infoText.setPrefWidth(400);
	            infoText.setPrefHeight(600);
	            
	            
	            Button closeButton = new Button("Close");
	            closeButton.setOnAction(closeEvent -> infoStage.close());

	            
	            VBox layout = new VBox(20, infoText, closeButton);
	            layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

	            Scene infoScene = new Scene(layout, 700, 700);
	            infoStage.setScene(infoScene);
	            infoStage.setResizable(false);
	            infoStage.show();
	        });

	        
	        
	        
	        mute.setOnAction(event -> {
	        	if (!mediaPlayer.isMute()) {
	        		try {
	        			speakerImageView.setImage(new Image(new FileInputStream("res/view/mute speaker.png")));
					} catch (Exception e) {
						e.printStackTrace();
					}
	        		speakerImageView.setFitHeight(30);
	        		speakerImageView.setFitWidth(30);
					mute.setGraphic(speakerImageView);
					mediaPlayer.setMute(true);
	        	}else {
	        		try {
	        			speakerImageView.setImage(new Image(new FileInputStream("res/view/speaker.png")));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        		speakerImageView.setFitHeight(30);
	        		speakerImageView.setFitWidth(30);
					mute.setGraphic(speakerImageView);
					mediaPlayer.setMute(false);
	        	}
	        });
	        
	        //close.setOnAction(event -> {
	        //	primaryStage.setScene(startScreen);
	        	//alertStage.close();
	        //});
	        Scene scene = new Scene(pane, 500, 200);
	        alertStage.setScene(scene);
	        alertStage.initOwner(primaryStage);
	        alertStage.setResizable(false);
	        
	        alertStage.initModality(Modality.WINDOW_MODAL);
	        alertStage.initOwner(primaryStage);
	        
	        alertStage.show();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	private void playBackgroundMusic() {
	    try {
	        String musicPath = new File("res/audio/background.mp3").toURI().toString(); // Use "file:" prefix for local files
	        Media media = new Media(musicPath);
	        mediaPlayer = new MediaPlayer(media);
	        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); 
	        mediaPlayer.play();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	private void playWinMusic() {
	    try {
	        String musicPath = new File("res/audio/win.mp3").toURI().toString(); // Use "file:" prefix for local files
	        Media media = new Media(musicPath);
	        mediaPlayer = new MediaPlayer(media);
	        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); 
	        mediaPlayer.play();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	private void playPopupSound() {
	    try {
	        String soundPath = new File("res/audio/popup.mp3").toURI().toString();
	        Media media = new Media(soundPath);
	        MediaPlayer popupPlayer = new MediaPlayer(media);
	        popupPlayer.play();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void checkAndRefillDeck(ArrayList<Card> firepit, GameView gameView) {
	    if (firepit.size() == 52) {
	        // Refill the deck with all cards from the firepit
	        Deck.refillPool(firepit);
	        firepit.clear();

	        // Update the UI to show that the deck is now full again
	        gameView.updateDeckVisibility(false);
	    }
	    if (Deck.getPoolSize() == 0) {
	        gameView.updateDeckVisibility(true);
	    }

	}

	
	public static void main(String[] args) {
		launch(args);
	}
	
}
