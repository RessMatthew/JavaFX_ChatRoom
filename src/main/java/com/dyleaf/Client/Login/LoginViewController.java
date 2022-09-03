package com.dyleaf.Client.Login;

import com.dyleaf.Client.model.ClientModel;
import com.dyleaf.Client.stage.ControlledStage;
import com.dyleaf.Client.MainApp;
import com.dyleaf.Client.stage.StageController;
import com.dyleaf.Dao.DbUtils;
import com.dyleaf.bean.ServerUser;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginViewController implements ControlledStage, Initializable {

    @FXML
    PasswordField textPassword;
    @FXML
    TextField txtUsername;
    @FXML
    Button btn_login;
    @FXML
    Button btn_signIn;

    StageController myController;
    ClientModel model;

    public LoginViewController() {
        super();
    }


    public void setStageController(StageController stageController) {
        this.myController = stageController;
        model = ClientModel.getInstance();
    }

    public void initialize(URL location, ResourceBundle resources) {

    }

    //跳转到主界面
    public void goToMain() {
        myController.loadStage(MainApp.mainViewID,MainApp.mainViewRes);
        myController.setStage(MainApp.mainViewID,MainApp.loginViewID);
        myController.getStage(MainApp.mainViewID).setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                model.disConnect();
                myController.unloadStage(MainApp.EmojiSelectorID);
            }
        });
    }

    //跳转到注册界面
    public void goToReg(){
        myController.loadStage(MainApp.regViewID,MainApp.regViewRes);
        myController.setStage(MainApp.regViewID,MainApp.loginViewID);
    }

    public void logIn() {
        StringBuffer result = new StringBuffer();
        if (model.CheckLogin(txtUsername.getText(), textPassword.getText(), result, 0)) {
            goToMain();
        } else {
            showError(result.toString());
        }
    }

    /**
     * 最小化窗口
     * @param event
     */
    @FXML public void minBtnAction(ActionEvent event){
        Stage stage = myController.getStage(MainApp.loginViewID);
        stage.setIconified(true);
    }
    /**
     * 关闭窗口，关闭程序
     * @param event
     */
    @FXML public void closeBtnAction(ActionEvent event){
        Platform.exit();
        System.exit(0);
    }

    public void showError(String error) {
        // TODO: 17-11-1
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Wechat");
        alert.setContentText("登录失败 " + error);
        alert.show();
    }

//    public void signUp(ActionEvent actionEvent) {
//        StringBuffer result = new StringBuffer();
//        if (model.CheckLogin(txtUsername.getText(), textPassword.getText(), result, 1)) {
//            goToMain();
//        } else {
//            showError(result.toString());
//        }
//    }

}