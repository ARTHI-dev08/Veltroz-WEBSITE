async function scheduleCall(){

    console.log("Button clicked");

    const data = {

        name:
            document.getElementById(
                "name"
            ).value,

        email:
            document.getElementById(
                "email"
            ).value,

        phone:
            document.getElementById(
                "phone"
            ).value,

        time:
            document.getElementById(
                "time"
            ).value,

        message:
            document.getElementById(
                "message"
            ).value
    };

    console.log(data);

    try{

        const response =
            await fetch(

                "http://localhost:8081/api/schedule",

                {

                    method: "POST",

                    headers: {

                        "Content-Type":
                        "application/json"
                    },

                    body:
                        JSON.stringify(data)
                }
            );

        const result =
            await response.text();

        alert(result);

    }catch(error){

        console.log(error);

        alert(
            "Schedule Failed"
        );
    }
}