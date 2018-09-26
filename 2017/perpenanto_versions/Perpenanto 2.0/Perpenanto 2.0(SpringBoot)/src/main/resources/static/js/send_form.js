'use strict';

$("form").submit(function(event){

    event.preventDefault();

    let post_url = $(this).attr("action");
    let request_method = $(this).attr("method");
    let form_data = $(this).serialize();


    $.ajax({
        url : post_url,
        type: request_method,
        data : form_data,
        success: function(){
            location.reload();
        }
    });

});