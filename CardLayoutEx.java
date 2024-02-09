import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class CardLayoutEx extends JFrame{
    JPanel cards;//card panel
    final static String firstPanel = "Select Size", secondPanel = "Select Toppings", thirdPanel = "Takeout/Delivery", fourthPanel = "Receipt";//panel labels
    String finSize="";//final size of pizza
    double finPrice=0;//final price of pizza
    ArrayList<String> finToppings= new ArrayList<String>();//final list of toppings of pizza
    JLabel priceLabel = new JLabel(), sizeLabel = new JLabel(), toppingsLabel=new JLabel(), dtLabel=new JLabel();//labels for receipt
    //takeout/delivery variables
    int takeoutOption=-1;
    boolean deliveryBool=false;
    String deliveryAddress="";

    //Updates labels on Receipt Card - price, toppings, takeout/delivery, etc.
    public void updateFinLabels(){
        //calculate total price of pizza
        switch(finSize){
            case "Small":{
                finPrice=5;
                break;
            }
            case "Medium":{
                finPrice=10;
                break;
            }
            case "Large":{
                finPrice=15;
                break;
            }
            case "Super":{
                finPrice=20;
                break;
            }
            default: finPrice=0;
        }
        int toppingNum = finToppings.size();
        if(finToppings.contains("Extra Cheese")) toppingNum--;
        switch(toppingNum){
            case 1:{
                finPrice+=0.5;
                break;
            }
            case 2:{
                finPrice+=1;
                break;
            }
            case 3:{
                finPrice+=1.25;
                break;
            }
            default: break;
        }

        //updating final variables/labels
        if(!deliveryAddress.isEmpty()&&!deliveryBool){
            deliveryBool=true;
            finPrice+=10;
            dtLabel.setText("Deliver: "+deliveryAddress);
        }
        else if(takeoutOption!=-1){
            String[] list = {"ASAP", "30 Minutes", "1 Hour", "2 Hours"};
            dtLabel.setText("Takeout: "+list[takeoutOption]);
        }
        priceLabel.setText("$"+finPrice);
        sizeLabel.setText("Size: "+finSize);
        toppingsLabel.setText("Toppings: "+finToppings.toString());
    }//end calcTotal

    /*
    Adds everything to the panes... everything
    */
    public void addComponentToPane(Container pane) {
        JButton nextButton1 = new JButton("Next"), nextButton2 = new JButton("Next"), nextButton3 = new JButton("Next");
        JButton previousButton = new JButton("Previous");

        //Create the cards

        //Card 1 - Size
        JPanel card1 = new JPanel();

        //set up radio buttons
        JRadioButton small = new JRadioButton("Small - $5");
        JRadioButton medium = new JRadioButton("Medium - $10");
        JRadioButton large = new JRadioButton("Large - $15");
        JRadioButton supe = new JRadioButton("Super - $20");
        ButtonGroup sizeGroup = new ButtonGroup();
        JRadioButton[] sizeButtons = {small, medium, large, supe};

        //ActionListener for RadioButtons
        //records the final size from the selected button
        ActionListener rbListener = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                for(JRadioButton size : sizeButtons){
                    if(size.isSelected()) finSize = size.getText().split(" ")[0];
                }
            }

        };//end ActionListener

        //add components to ButtonGroup, and to panel
        for(JRadioButton size : sizeButtons){
            sizeGroup.add(size);
            size.addActionListener(rbListener);
            card1.add(size);
        }
        card1.add(nextButton1);

        //Card 2 - Toppings
        JPanel card2 = new JPanel();
        card2.setLayout(new GridBagLayout());
        GridBagConstraints n = new GridBagConstraints();
        JLabel tPrice = new JLabel("$0.00");

        //initialize toppings JCheckBoxes
        JCheckBox top1 = new JCheckBox("Peppers");
        JCheckBox top2 = new JCheckBox("Mushrooms");
        JCheckBox top5 = new JCheckBox("Olives");
        JCheckBox top6 = new JCheckBox("Pineapple");
        JCheckBox top3 = new JCheckBox("Sausage");
        JCheckBox top4 = new JCheckBox("Pepperoni");
        JCheckBox top7 = new JCheckBox("Chicken");
        JCheckBox top8 = new JCheckBox("Ham");
        JCheckBox cheese = new JCheckBox("Extra Cheese");
        //add toppings to array
        JCheckBox[] toppings = {top1, top2, top5, top6, top3, top4, top7, top8};


        /*
           ActionListener for JCheckBoxes
           sees how many checkboxes are selected, updates text with price accordingly (1 topping = $0.50...)
           if three checkboxes are selected, voids other checkboxes until one is deselected
           adds toppings to public Arraylist to be printed later in "receipt"
         */
        ActionListener checkClick = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int checkCount=0;

                if (cheese.isSelected() && !finToppings.contains(cheese.getText())) finToppings.add(cheese.getText());
                else if (!cheese.isSelected()) finToppings.remove(cheese.getText());

                for(JCheckBox topping :toppings){
                    if(topping.isSelected()){
                        checkCount++;
                        if(!finToppings.contains(topping.getText())) finToppings.add(topping.getText());
                    }
                    else finToppings.remove(topping.getText());
                }
                if(checkCount==0) tPrice.setText("$0.00");
                else if(checkCount==1) tPrice.setText("$0.50");
                else if(checkCount==2) tPrice.setText("$1.00");
                if(checkCount==3){
                    tPrice.setText("$1.25");
                    for(JCheckBox topping : toppings){
                        if(!topping.isSelected()) topping.setEnabled(false);
                    }
                }
                else{
                    for(JCheckBox topping : toppings){
                        if(!topping.isEnabled()) topping.setEnabled(true);
                    }
                }
            }
        };//end ActionListener

        //add components to card2 using GridBagConstraints
        card2.add(tPrice, n);
        n.gridy=1;
        for (JCheckBox topping : toppings){
            topping.addActionListener(checkClick);
            card2.add(topping, n);
            n.gridy+=1;
        }
        cheese.addActionListener(checkClick);
        card2.add(cheese, n);
        n.gridy+=1;
        card2.add(previousButton, n);
        card2.add(nextButton2, n);

        //Card 3 - Takeout/Delivery
        JPanel card3 = new JPanel();
        card3.setLayout(new GridBagLayout());
        GridBagConstraints m = new GridBagConstraints();
        JRadioButton takeout = new JRadioButton("Takeout");
        JRadioButton delivery = new JRadioButton("Delivery - $10");
        ButtonGroup methodGroup = new ButtonGroup();
        JRadioButton[] tdButtons = {takeout, delivery};
        JLabel text = new JLabel();

        //ActionListener for RadioButtons
        //Displays OptionDialog or InputDialog to get further information from user regarding
        //pizza acquisition technique
        ActionListener tdActionListener = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(e.getSource()==takeout){
                    deliveryAddress="";
                    String[] options = {"ASAP", "30 Minutes", "1 Hour", "2 Hours"};
                    takeoutOption = JOptionPane.showOptionDialog(card3, "When would you like to pick up your pizza", "Pickup Time", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                    text.setText("Takeout: "+options[takeoutOption]);
                }
                else{
                    takeoutOption=-1;
                    deliveryAddress = JOptionPane.showInputDialog(card3, "Enter your address for delivery");
                    text.setText("Delivery: "+deliveryAddress);
                }

            }
        };//end tdActionListener

        m.gridy=0;
        //adds radio buttons to panel, add ActionListener to buttons
        for(JRadioButton button : tdButtons){
            methodGroup.add(button);
            button.addActionListener(tdActionListener);
            card3.add(button,m);
        }
        m.gridy++;
        card3.add(text,m);
        m.gridy++;
        card3.add(nextButton3,m);


        //Card 4 - Receipt
        JPanel card4 = new JPanel();
        card4.setLayout(new GridBagLayout());
        GridBagConstraints o = new GridBagConstraints();
        o.gridy=0;
        JLabel[] labelList = {sizeLabel, toppingsLabel, dtLabel, priceLabel};
        //add all components to panel
        for(JLabel label : labelList){
            card4.add(label,o);
            o.gridy++;
        }

        //Create the panel that contains the "cards".
        //add the cards to the panel
        cards = new JPanel(new CardLayout());
        cards.add(card1, firstPanel);
        cards.add(card2, secondPanel);
        cards.add(card3, thirdPanel);
        cards.add(card4, fourthPanel);
        CardLayout cl = (CardLayout)(cards.getLayout());

        //action listener for the next/previous buttons
        ActionListener buttonClick = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(e.getSource()==nextButton1 || e.getSource()==nextButton2 || e.getSource()==nextButton3){
                    updateFinLabels();
                    cl.next(cards);
                }
                if(e.getSource() == previousButton){
                    cl.previous(cards);
                }
            }
        };//end ActionListener

        //add ActionListener to buttons
        JButton[] buttons = {nextButton1, nextButton2, nextButton3, previousButton};
        for(JButton button : buttons){
            button.addActionListener(buttonClick);
        }

        //add the cards panel to the input pane
        pane.add(cards, BorderLayout.CENTER);
    }

    //main
    public static void main(String[] args) {
        //Create and set up the window.
        JFrame frame = new JFrame("CardLayoutDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane
        //Adds card layout to pane, including cards and all additional components
        CardLayoutEx demo = new CardLayoutEx();
        demo.addComponentToPane(frame.getContentPane());

        //Display the window.
        frame.pack();
        frame.setVisible(true);

    }//end main
}//end Pizza