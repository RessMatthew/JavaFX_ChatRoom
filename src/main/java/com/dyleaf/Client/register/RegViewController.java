package com.dyleaf.Client.register;

import com.dyleaf.Client.MainApp;
import com.dyleaf.Client.model.ClientModel;
import com.dyleaf.Client.stage.ControlledStage;
import com.dyleaf.Client.stage.StageController;
import com.dyleaf.Dao.DbUtils;
import com.dyleaf.bean.OssPutObject;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RegViewController implements ControlledStage {

    @FXML
    PasswordField textPassword;
    @FXML
    TextField txtUsername;
    @FXML
    TextField emailText;
    @FXML
    TextField checkText;
    @FXML
    Button btn_reg;//注册按钮
    @FXML
    Button btn_send;//发送验证码按钮
    @FXML
    Button btn_profile;//设置头像

    StageController myController;
    ClientModel model;
    String resultUrl="";//头像URL
    String code;//邮箱验证码

    private Stage stage;
    private StageController stageController;

    //跳转到登录界面
    public void goTologin() {
        myController.loadStage(MainApp.loginViewID,MainApp.loginViewRes, StageStyle.UNDECORATED);
        myController.setStage(MainApp.loginViewID,MainApp.regViewID);
    }

    public void setStageController(StageController stageController) {
        this.myController = stageController;
        model = ClientModel.getInstance();
    }

    //选择头像
    @FXML
    public void chooseProfile() throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Change profile");
        File file = fileChooser.showOpenDialog(stage);

        String url=file.toString();
        String fileName=file.getName();
        System.out.println(url);
        System.out.println(fileName);

        resultUrl= OssPutObject.putObject(url,fileName);//上传图片
        System.out.println(resultUrl);

        //设置头像
        String styleSheet="-fx-graphic:url("+resultUrl+")";
        btn_profile.setStyle(styleSheet);
    }

    //点击注册
    @FXML
    public void onRegBtnClicked(){
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "insert into ChatUser(name,password,image)values(?,?,?)";
        try{
            conn = DbUtils.getConnection();
            System.out.println("get connect");
            ps = conn.prepareStatement(sql);
            ps.setString(1, txtUsername.getText());
            ps.setString(2, textPassword.getText());
            ps.setString(3,resultUrl);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("用户添加失败");
        }finally{
            DbUtils.close(null, ps, conn);
        }

        if (code.equals(checkText.getText())){
            //注册成功对话框
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("注册");
            alert.setHeaderText(null);
            alert.setContentText("注册成功！");
            alert.showAndWait();
            goTologin();//跳转到登录界面
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("注册");
            alert.setHeaderText(null);
            alert.setContentText("验证码输入错误！请重新输入");
            alert.showAndWait();
        }

    }

    //发送邮箱验证码
    @FXML
    public void sendAuthCodeEmail() {
        try {
            SimpleEmail mail = new SimpleEmail();
            code=achieveCode();
            mail.setHostName("smtp.qq.com");//发送邮件的服务器
            mail.setAuthentication("2841872640@qq.com","dwkfsyoqhncrdecc");//刚刚记录的授权码，是开启SMTP的密码
            mail.setFrom("2841872640@qq.com","聊天室");  //发送邮件的邮箱和发件人
            mail.setSSLOnConnect(true); //使用安全链接
            mail.addTo(emailText.getText());//接收的邮箱
            mail.setSubject("注册验证码");//设置邮件的主题
            mail.setMsg("尊敬的用户:你好!\n 注册验证码为:" + code);//设置邮件的内容
            mail.send();//发送

            System.out.println("验证码为:"+code);

        } catch (EmailException e) {
            e.printStackTrace();
        }
    }

    //生成随机验证码
    public String achieveCode() {  //由于数字 1 、 0 和字母 O 、l 有时分不清楚，所以，没有数字 1 、 0
        String[] beforeShuffle= new String[] { "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F",
                "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a",
                "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
                "w", "x", "y", "z" };
        List list = Arrays.asList(beforeShuffle);//将数组转换为集合
        Collections.shuffle(list);  //打乱集合顺序
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i)); //将集合转化为字符串
        }
        return sb.toString().substring(3, 8);  //截取字符串第4到8
    }

}
