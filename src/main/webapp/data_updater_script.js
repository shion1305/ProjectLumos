function update() {
    var req = new XMLHttpRequest();
    req.open("POST", "https://shion1305.com/Lumos/updateDate");
    req.send(document.getElementById("data_textarea").value);
    document.getElementById("status").innerHTML = "確認中...";
    req.onreadystatechange = function () {
        if (req.readyState === 4) {
            switch (req.status) {
                case 404:
                    document.getElementById("status").innerHTML = "更新に失敗しました";
                    break;
                case 202:
                    document.getElementById("status").innerHTML = "更新に成功しました";
                    break;
                case 400:
                    document.getElementById("status").innerHTML = "フォーマットに誤りがあります";
                    break;
                default:
                    document.getElementById("status").innerHTML = "予測しないエラーが発生しました " + req.status.toString();
            }
        }
    }
}