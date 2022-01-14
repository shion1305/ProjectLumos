package com.shion1305.lumos;

import okhttp3.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.sql.Connection;
import java.util.Properties;

public class LineSender {
    MediaType mType;
    OkHttpClient client;
    String token;

    public static void main(String[] args) {
//        String lineToken = ConfigManager.getConfig("LineMessagingToken");
//                LineSender sender = new LineSender(lineToken);
//        String message = "{\"type\": \"bubble\",\"hero\": {\"type\": \"image\",\"url\": \"https://scdn.line-apps.com/n/channel_devcenter/img/fx/01_1_cafe.png\",\"size\": \"full\",\"aspectRatio\": \"20:13\",\"aspectMode\": \"cover\",\"action\": {\"type\": \"uri\",\"uri\": \"http://linecorp.com/\"}},\"body\": {\"type\": \"box\",\"layout\": \"vertical\",\"contents\": [{\"type\": \"text\",\"text\": \"Lumos-出欠確認!\",\"weight\": \"bold\",\"size\": \"xl\"},{\"type\": \"box\",\"layout\": \"vertical\",\"margin\": \"lg\",\"spacing\": \"sm\",\"contents\": [{\"type\": \"box\",\"layout\": \"baseline\",\"spacing\": \"sm\",\"contents\": [{\"type\": \"text\",\"text\": \"以下の日程でMTGを開く予定です!\",\"wrap\": true,\"color\": \"#666666\",\"size\": \"md\",\"flex\": 5,\"margin\": \"md\"}]},{\"type\": \"box\",\"layout\": \"baseline\",\"spacing\": \"sm\",\"contents\": [{\"type\": \"text\",\"text\": \"Time\",\"color\": \"#aaaaaa\",\"size\": \"sm\",\"flex\": 1},{\"type\": \"text\",\"text\": \"" + "\",\"wrap\": true,\"color\": \"#666666\",\"size\": \"sm\",\"flex\": 5}]}]}]},\"footer\": {\"type\": \"box\",\"layout\": \"vertical\",\"spacing\": \"sm\",\"contents\": [{\"type\": \"button\",\"style\": \"primary\",\"height\": \"sm\",\"action\": {\"type\": \"message\",\"label\": \"参加予定!\",\"text\": \"参加予定\"}},{\"type\": \"button\",\"style\": \"secondary\",\"height\": \"sm\",\"action\": {\"type\": \"message\",\"label\": \"遅れて参加!\",\"text\": \"遅れて参加!\"}},{\"type\": \"button\",\"style\": \"secondary\",\"height\": \"sm\",\"action\": {\"type\": \"message\",\"label\": \"欠席します\",\"text\": \"欠席します\"}}],\"flex\": 0}}";
        String serverName = "localhost";
        String mydatabase = "mydatabase";
        String url = "jdbc:mysql://" + serverName + "/" + mydatabase;
        String username = "username";
        String password = "password";

        String connectionURL = "jdbc:sqlserver://shion1305.v2003.coreserver.jp;DatabaseName=shion1305_lumos-line;user=shion1305_lumos-line;password=lumos202110!!!";
        Connection con = null;
        try {
            //Create the connection using the static getConnection method
            con = DriverManager.getConnection(connectionURL);
            Statement stmt = con.createStatement();

//            Execute the SQL statement and get the results in a Resultset
//            ResultSet rs = stmt.executeQuery("select moviename, releasedate from movies");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void send(String messageJson, LineProfile[] lineProfile) {
        StringBuilder toward = new StringBuilder();
        if (lineProfile.length < 1) return;
        else if (lineProfile.length == 1) {
            toward.append(lineProfile[0]);
        } else {
            toward.append("{");
            for (int i = 0; i < lineProfile.length; i++) {
                if (i != 0) {
                    toward.append(",");
                }
                toward.append(lineProfile[i].userId);
            }
            toward.append("}");
        }
        String json1 = "{\"to\": " + toward.toString() + ",\"messages\": [{\"type\": \"flex\",\"altText\": \"This is a Flex Message\",\"contents\": " + messageJson +
                "}]}";
        RequestBody body = RequestBody.create(json1, mType);
        Request request = new Request.Builder()
                .url("https://api.line.me/v2/bot/message/push")
                .addHeader("Authorization", "Bearer " + token)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.out.println(response);
                throw new IOException("Unexpected code " + response);
            }
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public LineSender(String token) throws IOException {
        this.token = token;
        mType = MediaType.parse("application/json; charset=utf-8");
        client = new OkHttpClient();
    }
}