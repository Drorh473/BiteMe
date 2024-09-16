package gui;


import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import EnumsAndConstants.TypeOfOrder;
import EnumsAndConstants.UserType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.util.Callback;
import logic.Orders.Delivery;
import logic.Orders.Order;
import logic.Users.User;

public class PickUpAndFutureController {
	
	@FXML
	private Text top;
	
	@FXML
	private Button btnChooseRestuarant = null;

	@FXML
	private Button btnBack = null;
	
	@FXML
	private TextField Name;
	
	@FXML
	private Text error;

	@FXML
	private TextField Phone;
	
	@FXML
	private MenuButton timeMenuButton;
	
	@FXML
	private DatePicker date;

	private Order order;
	private User user;
	private Delivery delivery;
	private Delivery buisnesDelivery;
	private Helper helper = new Helper();

	public PickUpAndFutureController(Order order, User user) {
		this.order = order;
		this.user = user;
		
	}

	
	public PickUpAndFutureController(Order order, User user, Delivery delivery) {
		this.order = order;
		this.user = user;
		buisnesDelivery=delivery;
	}


	@FXML
	public void initialize() {
		if(order.getType().equals(TypeOfOrder.PickUp)) {
			top.setText("Pick-Up");
		}
//		if(order.getType().equals(TypeOfOrder.PRE)) {
//			top.setText("Future-Order");
//		}
		Name.setText(user.getFirstName() + " " + user.getLastName());
		Phone.setText(user.getPhoneNumber());
		initializeTimeMenuItems();
		intializeDate();
	
	}

	
	private void intializeDate() {
		date.setDayCellFactory(new Callback<DatePicker, DateCell>() {
			public DateCell call(final DatePicker datePicker) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);

						// Disable all dates before today
						if (item.isBefore(LocalDate.now())) {
							setDisable(true);
							setStyle("-fx-background-color: #ffc0cb;"); // Optional: add a style
						}
					}
				};
			}
		});
	}

	
	@FXML
	public void choose(ActionEvent event) throws Exception {
		StringBuilder sb = new StringBuilder();
		// Check if all fields are filled
		if (Name.getText().isEmpty() || Phone.getText().isEmpty() || 
			timeMenuButton.getText().equals("Select Time") || date.getValue() == null) {
				sb.append("Please fill all fields.\n");
		}

		// Validate the phone number 
		String phoneNumber = Phone.getText();
	    if (!phoneNumber.matches("^\\d{3}-\\d{7}$")) {
	    	sb.append("Your phone number should match the pattern '050-1234567'.\n");
		}
	    
	    // Display error messages if any
		if (sb.length() > 0) {
			error.setVisible(true);
		    error.setText(sb.toString());
	        return; 
	    } else {
	    	delivery = new Delivery();
	    	ArrayList<String> usernames = new ArrayList<>();
			usernames.add(user.getUserName());
			delivery.setUsernamesOfParticipants(usernames);
			delivery.setName(Name.getText());
			delivery.setPhoneNumber(Phone.getText());
			delivery.setType(order.getType());
			delivery.setOrderId(order.getOrderId());
			delivery.setNumOfParticipants(1);
			order.setDeliveryID(delivery.getDeliveryId());
	        LocalDate localDate = date.getValue();
	        // Convert LocalDate to java.sql.Date
	        java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);
	        order.setAskedByCustomerDate(sqlDate);
	        String timeString = timeMenuButton.getText() + ":00"; // Convert to hh:mm:ss format
	        java.sql.Time sqlTime = java.sql.Time.valueOf(timeString);
			// Convert java.sql.Time to java.time.LocalTime
			LocalTime localTime = sqlTime.toLocalTime();

			// Subtract 3 hours and 30 minutes
			LocalTime updatedTime = localTime.minusHours(3).minusMinutes(30);

			// Convert back to java.sql.Time
			Time updatedSqlTime = Time.valueOf(updatedTime);
	        order.setAskedByCustomerTime(updatedSqlTime);
	        
	     // Check if the chosen time is more than 2 hours from now
	        LocalDate selectedDate = date.getValue();
	        LocalTime selectedTime = LocalTime.parse(timeMenuButton.getText()); // Assuming timeMenuButton contains the time in hh:mm format
	        LocalDateTime selectedDateTime = LocalDateTime.of(selectedDate, selectedTime);

	        // Get the current date and time
	        LocalDateTime currentDateTime = LocalDateTime.now();

	        // Add 2 hours to the current date and time
	        LocalDateTime twoHoursLater = currentDateTime.plusHours(2);

	        // Set the order type based on the time difference
	        if (selectedDateTime.isAfter(twoHoursLater)) {
	            order.setType(TypeOfOrder.PRE);
	        } else {
	            order.setType(TypeOfOrder.PickUp);
	        }
	        
	        if(user.getUserType().equals(UserType.BusinessCustomer))
	        {
				Object controller = new RestaurantController(order,user, buisnesDelivery);
				helper.newGui("Restaurant Page", "/gui/RestaurantPage.fxml", controller);
				((Node) event.getSource()).getScene().getWindow().hide();
	        }
	        else {
			Object controller = new RestaurantController(order,user, delivery);
			helper.newGui("Restaurant Page", "/gui/RestaurantPage.fxml", controller);
			((Node) event.getSource()).getScene().getWindow().hide();
	        }
		}
	}

	
	@FXML
	public void back(ActionEvent event) throws Exception {
		Object controller = new StartOrderController(user);
		helper.newGui("Start order", "/gui/StartOrder.fxml", controller);
		((Node) event.getSource()).getScene().getWindow().hide();
	}
	
	
	private void initializeTimeMenuItems() {
	    date.valueProperty().addListener((observable, oldValue, newValue) -> updateTimeMenuItems(newValue));
	    updateTimeMenuItems(LocalDate.now()); // Initialize with today's date
	}

	private void updateTimeMenuItems(LocalDate selectedDate) {
	    timeMenuButton.getItems().clear(); // Clear existing items

	    int startHour;
	    int startMinute;

	    if (selectedDate.equals(LocalDate.now())) {
	        LocalTime currentTime = LocalTime.now().plusMinutes(30);
	        startHour = currentTime.getHour();
	        startMinute = currentTime.getMinute();
	        
	        // Round minutes to the nearest 10-minute mark
	        startMinute = (startMinute / 10) * 10;
	        if (startMinute >= 60) {
	            startMinute = 0;
	            startHour += 1;
	        }
	    } else {
	        startHour = 12;
	        startMinute = 0;
	    }

	    for (int hour = startHour; hour < 24; hour++) {
	        for (int minute = (hour == startHour) ? startMinute : 0; minute < 60; minute += 10) {
	            String timeString = String.format("%02d:%02d", hour, minute);
	            MenuItem timeItem = new MenuItem(timeString);
	            timeItem.setOnAction(e -> timeMenuButton.setText(timeString));
	            timeMenuButton.getItems().add(timeItem);
	        }
	    }
	}
}
