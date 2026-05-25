function sendMessage() {

    const inputField =
        document.getElementById("userInput");

    const messages =
        document.getElementById("messages");

    let originalInput =
        inputField.value.trim();

    if(originalInput === "") {
        return;
    }

    let input =
        originalInput.toLowerCase();

    let response =
        "Sorry, I couldn't understand your question. Please ask about Veltroz, our services, AI technology, robotics, automation, CEO details, contact information, or mission.";

    // COMPANY DETAILS

    if (
        input.includes("veltroz") ||
        input.includes("company") ||
        input.includes("about")
    ) {

        response =
            "Veltroz - Vision of Innovation is a modern technology company focused on IT services, AI technology innovation, robotics, automation, and digital transformation solutions.";
    }

    // SERVICES

    else if (
        input.includes("service") ||
        input.includes("services") ||
        input.includes("solution") ||
        input.includes("solutions")
    ) {

        response =
            "Veltroz provides IT services and solutions, AI technology innovation, robotics systems, automation solutions, website development, software development, and smart digital products.";
    }

    // AI TECHNOLOGY

    else if (
        input.includes("ai") ||
        input.includes("artificial intelligence") ||
        input.includes("innovation")
    ) {

        response =
            "We specialize in AI technology innovation, intelligent automation systems, smart assistants, and future-ready digital solutions.";
    }

    // ROBOTICS

    else if (
        input.includes("robot") ||
        input.includes("robotics")
    ) {

        response =
            "Veltroz works on robotics technologies and smart automation systems for modern industries and businesses.";
    }

    // AUTOMATION

    else if (
        input.includes("automation") ||
        input.includes("automatic")
    ) {

        response =
            "Our automation solutions help businesses improve efficiency using intelligent digital technologies and smart systems.";
    }

    // WEBSITE DEVELOPMENT

    else if (
        input.includes("website") ||
        input.includes("web development") ||
        input.includes("web")
    ) {

        response =
            "We develop modern, responsive, and scalable websites for startups, businesses, and organizations.";
    }

    // SOFTWARE

    else if (
        input.includes("software") ||
        input.includes("application") ||
        input.includes("app")
    ) {

        response =
            "Veltroz develops smart software applications, business solutions, and innovative digital platforms.";
    }

    // CEO DETAILS

    else if (
        input.includes("ceo") ||
        input.includes("founder") ||
        input.includes("owner") ||
        input.includes("team")
    ) {

        response =
            "The CEOs of Veltroz are Vigneshwar P and Sabari Kannan R.";
    }

    // CONTACT DETAILS

    else if (
        input.includes("contact") ||
        input.includes("phone") ||
        input.includes("mobile") ||
        input.includes("number")
    ) {

        response =
            "You can contact Veltroz at +91 8072713542.";
    }

    // EMAIL

    else if (
        input.includes("email") ||
        input.includes("mail")
    ) {

        response =
            "You can contact our support team through the official Veltroz email service.";
    }

    // LOCATION

    else if (
        input.includes("location") ||
        input.includes("address") ||
        input.includes("office") ||
        input.includes("where")
    ) {

        response =
            "Veltroz operates as a modern technology and innovation company providing digital solutions globally.";
    }

    // MISSION

    else if (
        input.includes("mission") ||
        input.includes("vision") ||
        input.includes("goal")
    ) {

        response =
            "Our mission is to create innovative, intelligent, and future-ready technology solutions through AI, robotics, automation, and digital transformation.";
    }

    // HELP

    else if (
        input.includes("help") ||
        input.includes("support")
    ) {

        response =
            "I can help you with company details, services, AI solutions, robotics, automation, CEO information, and contact details.";
    }

    // GREETINGS

    else if (
        input.includes("hi") ||
        input.includes("hello") ||
        input.includes("hey")
    ) {

        response =
            "Hello 👋 Welcome to Veltroz - Vision of Innovation. How can I help you today?";
    }

    // THANK YOU

    else if (
        input.includes("thank") ||
        input.includes("thanks")
    ) {

        response =
            "You're welcome 😊 Thank you for connecting with Veltroz.";
    }

    // USER MESSAGE

    messages.innerHTML += `

        <div class="user-msg">

            ${originalInput}

        </div>

    `;

    // BOT MESSAGE

    messages.innerHTML += `

        <div class="bot-msg">

            ${response}

        </div>

    `;

    // AUTO SCROLL

    messages.scrollTop =
        messages.scrollHeight;

    // CLEAR INPUT

    inputField.value = "";
}
document
    .getElementById("userInput")
    .addEventListener(
        "keypress",
        function(event){

            if(event.key === "Enter"){

                sendMessage();
            }
        }
    );