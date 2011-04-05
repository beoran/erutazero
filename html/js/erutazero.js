

Erutazero = new Object();

Erutazero.Game = new Object();
Erutazero.Game.prototype  = Erutazero.Game

Erutazero.Game.initialize = function() {
  this._canvas  = Beja.id('canvas');
  this._draw    = null;
  if (this._canvas && this._canvas.getContext) {
  //check whether browser support getting canvas context
    this._draw = this._canvas.getContext('2d');
  } else {
    throw("Canvas not supported");
  } 
  var x = 10;
  var y = 10;
  
  this._draw.fillStyle = "rgb(127,0,0)";
  this._draw.fillRect(x, y, 100, 85);
  
  x += 120;
  this._draw.strokeStyle = "rgb(0,0,0)";
  this._draw.strokeRect(0, 0, 640, 480);
  x = 10;
  y += 120;
  this._draw.fillStyle = "rgb(127,255,0)";
  this._draw.font = "bold 26px serif";
  this._draw.fillText('Test text ', x, y);

  y += 20;
  var img = new Image();
  img.src = "../Content/Images/cos2a.gif";
  img.onload = function () {
      this._draw.drawImage(img, x, y);
  }
  
}  


 
 
Beja.onload = function () {
  Erutazero.Game.initialize();
}
 
 























