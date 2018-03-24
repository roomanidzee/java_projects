function sendFile(file){

    var formData = new FormData();
    formData.append("file", file);

    var xhr = new XMLHttpRequest();

    xhr.open("POST", "/user/send_photo", true);
    xhr.send(formData);

    alert("Фото успешно отправлено!");

}