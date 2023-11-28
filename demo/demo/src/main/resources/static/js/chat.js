let stompClient = null;

function connectWebSocket() {
	const socket = new SockJS('/ws');
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
		console.log('Connected: ' + frame);
		stompClient.subscribe('/topic/messages', function(response) {
			showMessage(JSON.parse(response.body).content);
		}, function(error) {
        console.error('Error during WebSocket connection:', error);
        // Retry the connection after a delay
        setTimeout(connectWebSocket, 5000); // Retry after 5 seconds
    });
	});
}

function showMessage(message) {
	$("#message-container-table").prepend(`<tr><td><b>${message.name} :</b> ${message.content}</td></tr>`);
}


function sendMessage() {
	let jsonOb = {
		content: $("#message-value").val()
	}

	if (stompClient && stompClient.connected) {
		stompClient.send("/app/message", {}, JSON.stringify(jsonOb));
		$("#message-value").val(''); // Clear the input field
	} else {
		console.error('WebSocket is not connected');
	}
}

jQuery(document).ready((e) => {

	$("#login").click(() => {
		let name = $("#username").val();
		localStorage.setItem("name", name);
		$("#name-title").html(`Welcome , <b>${name} </b>`);
		connectWebSocket();
	})

	$('#chat-form').click(() => {
		sendMessage()
		//  e.preventDefault();
		// const messageInput = $('#message');
		//const message = messageInput.val();
		//console.log(message, "message");
		//stompClient.send("/app/chat", {}, JSON.stringify({ 'content': message }));
		//  messageInput.val('');
	});

});
