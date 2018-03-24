function createCookie(){
    document.cookie = "status=id;";
    window.location.reload();
}

function deleteCookie(){
    var date = new Date(1);
    document.cookie = "status=id; expires=" + date.toUTCString();
    window.location.reload();
}

function changeCookie(status){
    document.cookie = "status=" + status + ";";
    window.location.reload();
}