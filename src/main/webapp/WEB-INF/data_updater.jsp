<%@ page import="java.io.FileInputStream" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Lumos BOT活動日登録</title>
    <link href="https://shion1305.com/Lumos/data_updater_css.css" rel="stylesheet" type="text/css">
    <meta name="viewport" content="width=device-width,initial-scale=1.0">
    <link href="https://fonts.googleapis.com" rel="preconnect">
    <link crossorigin href="https://fonts.gstatic.com" rel="preconnect">
    <link href="https://fonts.googleapis.com/css2?family=Kiwi+Maru:wght@300&family=Teko:wght@300&family=Zen+Kurenaido&display=swap"
          rel="stylesheet">
</head>
<body>
<header>
    <div class="header_logo">
        <img src="https://shion1305.com/Lumos/logo.png" alt="bot logo image">
    </div>
    <div class="header_title">
        <h1>Lumos Activity BOT</h1>
        <h2>活動日設定ページ</h2>
    </div>
</header>
<div class="content">
    <h1>例に従って活動日を入力し、更新ボタンを押してください。</h1>
    <p>例:<br>2022/01/22 21:00</p>
    <% String data;
        try (FileInputStream stream = new FileInputStream(System.getProperty("user.home") + "/Lumos/schedule.data")) {
            data = new String(stream.readAllBytes());
        } %>
    <textarea id="data_textarea"><%=data%></textarea>
    <button onclick="update()" class="update_button">更新</button>
    <div id="status"></div>
    <script type="text/javascript" src="https://shion1305.com/Lumos/data_updater_script.js">
    </script>
</div>
<footer>
    <div class="footer-div">
        <div class="footer-logo">
            <img src="https://shion1305.com/logo.png" alt="logo of shion1305.com">
            <img src="https://shion1305.com/logo_name.png" alt="title of shion1305.com">
        </div>
        <p>Copyright © 2022 Shion Ichikawa. All rights reserved.</p>
    </div>
</footer>
</body>
</html>
