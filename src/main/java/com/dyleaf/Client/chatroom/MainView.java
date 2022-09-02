package com.dyleaf.Client.chatroom;


import com.dyleaf.Client.MainApp;
import com.dyleaf.Client.emojis.EmojiDisplayer;
import com.dyleaf.Client.model.ClientModel;
import com.dyleaf.Client.stage.ControlledStage;
import com.dyleaf.Client.stage.StageController;
import com.dyleaf.Utils.AudioBase64Util;
import com.dyleaf.Utils.AudioRecognition;
import com.dyleaf.Utils.AudioRecorder;
import com.dyleaf.bean.ClientUser;
import com.dyleaf.bean.Message;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import static com.dyleaf.Utils.Constants.*;
import static com.dyleaf.Utils.Constants.CONTENT;
import static com.dyleaf.Utils.BinUtil.*;

public class MainView implements ControlledStage, Initializable {

    @FXML
    public Button btnEmoji;
    @FXML
    public TextArea textSend;
    @FXML
    public Button btnSend;
    @FXML
    public ListView chatWindow;
    @FXML
    public ListView userGroup;
//    @FXML
//    public Label labUserName;
    @FXML
    public Label labChatTip;
    @FXML
    public Label labUserCoumter;

//    @FXML
//    public BorderPane root;

    private Gson gson = new Gson();
    private StageController stageController;
    private ClientModel model;
    private static MainView instance;
    private boolean pattern = GROUP; //chat model
    private String seletUser = "[group]";
    private static String thisUser;
    private ObservableList<ClientUser> uselist;
    private ObservableList<Message> chatReccder;

    private Stage stage;


    public MainView() {
        super();
//        stage = getStage();
        instance = this;
    }

//    private Stage getStage() {
//        if (stage == null) {
//            stage = (Stage) root.getScene().getWindow();
//        }
//        return stage;
//    }

    public static MainView getInstance() {
        return instance;
    }

    @Override
    public void setStageController(StageController stageController) {
        this.stageController = stageController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        model = ClientModel.getInstance();
        uselist = model.getUserList();
        chatReccder = model.getChatRecoder();
        userGroup.setItems(uselist);
        chatWindow.setItems(chatReccder);
        thisUser = model.getThisUser();
//        labUserName.setText("Welcome " + model.getThisUser() + "!");

        //发送按钮的逻辑处理函数
        btnSend.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (pattern == GROUP) {
                    HashMap map = new HashMap();
                    map.put(COMMAND, COM_CHATALL);
                    map.put(CONTENT, textSend.getText().trim());
                    map.put(MESSAGETYPE,ORDINARYMESSAGE);
                    map.put(FILECONTENT,"");
                    model.sentMessage(gson.toJson(map));
                } else if (pattern == SINGLE) {
                    HashMap map = new HashMap();
                    map.put(COMMAND, COM_CHATWITH);
                    map.put(RECEIVER, seletUser);
                    map.put(SPEAKER, model.getThisUser());
                    map.put(CONTENT, textSend.getText().trim());
                    map.put(MESSAGETYPE,ORDINARYMESSAGE);
                    map.put(FILECONTENT,"");
                    model.sentMessage(gson.toJson(map));
                }
                textSend.setText("");
            }
        });

        //左边选择聊天用户
        userGroup.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            ClientUser user = (ClientUser) newValue;
            System.out.println("You are selecting " + user.getUserName());
            if (user.getUserName().equals("[group]")) {
                pattern = GROUP;
                if (!seletUser.equals("[group]")) {
                    model.setChatUser("[group]");
                    seletUser = "[group]";
                    labChatTip.setText("Group Chat");
                }
            } else {
                pattern = SINGLE;
                if (!seletUser.equals(user.getUserName())) {
                    model.setChatUser(user.getUserName());
                    seletUser = user.getUserName();
                    labChatTip.setText(seletUser);
                }
            }
        });

        //选择聊天消息
        chatWindow.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Message message = (Message) newValue;
            if(message!=null){
                if( (message.getType()!=null) && (message.getType().equals(FILETYPE) ) ) {

                    System.out.println("You are selecting ==> user:" + message.getSpeaker() + " time:" + message.getTimer());

                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Save file");
                    fileChooser.setInitialFileName(message.getContent());
                    File file = fileChooser.showSaveDialog(stage);
                    System.out.println(file);
                    if(file != null){
                        binToFile(message.getFileContent(), file.getName(),file.getParent());
//                        System.out.println("Path"+file.getPath());
//                        System.out.println("Parent"+file.getParent());
                    }
                }else if( (message.getType()!=null) && (message.getType().equals(VOICETYPE))){
                    System.out.println("-------音频消息-------");
                    AudioBase64Util.strToWar(message.getVoiceContent());
                    //测试播放
                    AudioRecorder audioRecorder = new AudioRecorder("test.wav");
                    audioRecorder.play("test.wav");
                }
            }
        });


        //右边聊天消息展示框
        chatWindow.setCellFactory(new Callback<ListView, ListCell>() {
            @Override
            public ListCell call(ListView param) {
                return new ChatCell();
            }
        });

        //左边联系人
        userGroup.setCellFactory(new Callback<ListView, ListCell>() {
            @Override
            public ListCell call(ListView param) {
                return new UserCell();
            }
        });
    }


    //点击表情按钮打开新的窗口
    @FXML
    public void onEmojiBtnClcked() {
        stageController.loadStage(MainApp.EmojiSelectorID, MainApp.EmojiSelectorRes);
        stageController.setStage(MainApp.EmojiSelectorID);
    }

    //文件传输按钮
    @FXML
    public void onFileBtnClicked() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save file");
        File file = fileChooser.showOpenDialog(stage);
        System.out.println(file);
        if(file != null){
            String fileContent = fileToBinStr(file);

            HashMap map = new HashMap();
            if (pattern == GROUP) {
                map.put(COMMAND, COM_CHATALL);
                map.put(SPEAKER, model.getThisUser());
            } else if (pattern == SINGLE) {
                map.put(COMMAND, COM_CHATWITH);
                map.put(RECEIVER, seletUser);
                map.put(SPEAKER, model.getThisUser());
            }
            map.put(CONTENT, file.getName());
            map.put(MESSAGETYPE,FILETYPE);
            map.put(FILECONTENT,fileContent);
            model.sentMessage(gson.toJson(map));
        }


    }

    //获取聊天输入框
    public TextArea getMessageBoxTextArea() {
        return textSend;
    }

    //服务器总人数
    public Label getLabUserCoumter() {
        return labUserCoumter;
    }

    //新建录音线程，并存入test.wav文件里
    AudioRecorder audioRecorder;
    //-------------------音频部分-----------------------
    public void onVoiceBtnPressed(MouseEvent mouseEvent) {
        audioRecorder = new AudioRecorder("test.wav");
        audioRecorder.start();
    }

    public void onVoiceBtnReleased(MouseEvent mouseEvent) throws IOException {
        audioRecorder.stopRecording();
        //audioRecorder.play("test.wav");//根据文件路径播放音频
        //转码发送
        try{
            String voiceContent = AudioBase64Util.wavToStr("test.wav");
            HashMap map = new HashMap();
            if (pattern == GROUP) {
                map.put(COMMAND, COM_CHATALL);
                map.put(SPEAKER, model.getThisUser());
            } else if (pattern == SINGLE) {
                map.put(COMMAND, COM_CHATWITH);
                map.put(RECEIVER, seletUser);
                map.put(SPEAKER, model.getThisUser());
            }
            String recognise = AudioRecognition.recognise();
            map.put(CONTENT, recognise);
            map.put(MESSAGETYPE,VOICETYPE);
            map.put(VOICECONTENT,voiceContent);
            model.sentMessage(gson.toJson(map));

        }catch (Exception e){
            e.printStackTrace();
        }
        /*
        HashMap map = new HashMap();
        if (pattern == GROUP) {
            map.put(COMMAND, COM_CHATALL);
            map.put(SPEAKER, model.getThisUser());
        } else if (pattern == SINGLE) {
            map.put(COMMAND, COM_CHATWITH);
            map.put(RECEIVER, seletUser);
            map.put(SPEAKER, model.getThisUser());
        }
        map.put(CONTENT, file.getName());
        map.put(MESSAGETYPE,FILETYPE);
        map.put(FILECONTENT,fileContent);
        model.sentMessage(gson.toJson(map));
        */

    }


    //-------------------渲染部分-----------------------

    //左边的一个个单个的联系人
    public static class UserCell extends ListCell<ClientUser> {
        @Override
        protected void updateItem(ClientUser item, boolean empty) {
            super.updateItem(item, empty);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (item != null) {
                        HBox hbox = new HBox();
                        ImageView imageHead = new ImageView(new Image("image/head.png"));
                        imageHead.setFitHeight(20);
                        imageHead.setFitWidth(20);
                        ClientUser user = (ClientUser) item;
                        ImageView imageStatus;
                        if(user.getUserName().equals("[group]")){
                            imageStatus = new ImageView(new Image("image/online.png"));
                        } else if(user.isNotify()==true){
                            imageStatus = new ImageView(new Image("image/message.png"));
                        }else {
                            if(user.getStatus().equals("online")){
                                imageStatus = new ImageView(new Image("image/online.png"));
                            }else{
                                imageStatus = new ImageView(new Image("image/offline.png"));
                            }
                        }
                        imageStatus.setFitWidth(20);
                        imageStatus.setFitHeight(20);
                        Label label = new Label(user.getUserName());
                        hbox.getChildren().addAll(imageHead, label,imageStatus);
                        setGraphic(hbox);
                    } else {
                        setGraphic(null);
                    }
                }
            });
        }
    }

//    右边的一条条消息
    public static class ChatCell extends ListCell<Message> {
        @Override
        protected void updateItem(Message item, boolean empty) {
            super.updateItem(item, empty);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    //inorder to avoid the
                    if (item != null) {
                        VBox box = new VBox();
                        HBox hbox = new HBox();
                        //聊天的内容
                        TextFlow txtContent = null;
                        if( (item.getType()!=null)&&(item.getType().equals(FILETYPE) ) ){
                            txtContent = new TextFlow(EmojiDisplayer.createEmojiAndTextNode("[文件]： "+item.getContent()));
                        }else if((item.getType()!=null)&&(item.getType().equals(VOICETYPE))){
                            /**
                             * TODO 渲染音频
                             **/
                            //String recognise = AudioRecognition.recognise();
                            txtContent = new TextFlow(EmojiDisplayer.createEmojiAndTextNode("[音频]： "+item.getContent()));
                            //txtContent = new TextFlow(EmojiDisplayer.createEmojiAndTextNode("[音频]： "+recognise));
                        }
                        else {
                            //如果不是文件类型，则正常渲染。
                            txtContent = new TextFlow(EmojiDisplayer.createEmojiAndTextNode(item.getContent()));
                        }
                        //聊天内容上方标签显示用户名字和发送时间
                        Label labUser = new Label(item.getSpeaker() + "[" + item.getTimer() + "]");
                        //聊天内容上方标签字体颜色和背景颜色
                        labUser.setStyle("-fx-background-color: #7bc5cd; -fx-text-fill: white;");
                        //用户头像
                        ImageView image = new ImageView(new Image("image/head.png"));
                        image.setFitHeight(20);
                        image.setFitWidth(20);
                        hbox.getChildren().addAll(image, labUser);
                        //己方发送的消息显示在右边
                        if (item.getSpeaker().equals(thisUser)) {
                            txtContent.setTextAlignment(TextAlignment.RIGHT);
                            hbox.setAlignment(Pos.CENTER_RIGHT);
                            box.setAlignment(Pos.CENTER_RIGHT);
                        }
                        box.getChildren().addAll(hbox, txtContent);
                        setGraphic(box);
                    } else {
                        setGraphic(null);
                    }
                }
            });
        }
    }

}
