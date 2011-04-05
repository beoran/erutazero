

Erutazero = new Object();

Erutazero.Game = new Object();
Erutazero.Game.prototype  = Erutazero.Game
 
 
Beja.onload = function () {
  var _canvas = document.getElementById('canvas');
  var _canvasContext = null;

  if (_canvas && _canvas.getContext) {
      //check whether browser support getting canvas context
      _canvasContext = _canvas.getContext('2d');
      var x = 10;
      var y = 10;
  
      _canvasContext.fillStyle = "rgb(127,0,0)";
      _canvasContext.fillRect(x, y, 100, 85);
      
      x += 120;
      _canvasContext.strokeStyle = "rgb(0,0,0)";
      _canvasContext.strokeRect(0, 0, 640, 480);
      x = 10;
      y += 120;
      _canvasContext.fillStyle = "rgb(127,255,0)";
      _canvasContext.font = "bold 26px sans-serif";
      _canvasContext.fillText('Test text ', x, y);
  
      y += 20;
      var img = new Image();
      img.src = "../Content/Images/cos2a.gif";
      img.onload = function () {
          _canvasContext.drawImage(img, x, y);
      }
  }

}
 
 























