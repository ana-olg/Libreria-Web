$(document).ready(function(){
	$.ajax({
		type: "GET",
		dataType: "html",
		url: "./ServletTest",
		data: $.param({
			usuario: "miguel", 
			tecnologia: "java"
		}),
		success: function(){
			console.log("Petición correcta");
		}
	})
});