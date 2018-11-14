$(document).ready(function(){
    $(".add-row").click(function(){
        const name = $("#name").val();
        const email = $("#email").val();

        const requestURL = "http://localhost:5000/api/v1/contract-launch";

        axios.put(requestURL, {
           service: "reactivecontracts-rabbit",
           project: "contract-launcher",
           num: "2"
        })
        .catch(err => console.log(err));

        window.location.replace("http://localhost:8080/index");
    });


    $(".delete-row").click(function(){
        $("table tbody").find('input[name="record"]').each(function(){
            if($(this).is(":checked")){
                axios.put(requestURL, {
                    service: "reactivecontracts-rabbit",
                    project: "contract-launcher",
                    num: "1"
                })
                .catch(err => console.log(err));

                window.location.replace("http://localhost:8080/index");
            }
        });
    });
});