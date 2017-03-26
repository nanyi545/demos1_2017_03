


function initPage(){

	$('div.main_content').click(function() {
        	regiser();
	});

}





function regiser(){
	alert("register!");
//	drawline();
	drawArc();
}



function drawline(){
    var canvas = document.getElementById('myCanvas');
    var context = canvas.getContext('2d');

    var width = canvas.width;   // get width
    var height = canvas.height;

          context.beginPath();
          context.moveTo(0, 0);
          context.lineTo(width/2, height/2);
          context.stroke();
}

function drawArc(){
    var canvas = document.getElementById('myCanvas');
          var context = canvas.getContext('2d');

          var x = canvas.width / 2;
          var y = canvas.height / 2;
          var radius = canvas.width / 4;
          var startAngle = 1 * Math.PI;
          var endAngle = 1.5 * Math.PI;
          var counterClockwise = false;

          context.beginPath();
//          context.arc(x, y, radius, startAngle, endAngle, counterClockwise);
          context.arc(100,75,50,0,2*Math.PI);

          context.lineWidth = 15;

          // line color
          context.strokeStyle = 'black';
          context.stroke();

}