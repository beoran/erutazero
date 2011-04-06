

Erutazero      = Beja.module();
Erutazero.game = Beja.module();


Erutazero.game.draw_test = function() {
  this._draw.fillStyle = "rgb(127,0,0)";
  this._draw.fillRect(10, 20, 100, 85);
  
  this._draw.strokeStyle = "rgb(0,0,0)";
  this._draw.strokeRect(0, 0, 640, 480);
  this._draw.fillStyle = "rgb(127,255,0)";
  this._draw.font = "bold 26px serif";
  this._draw.fillText('Test text OK!', 10, 240);

  var img     = new Image();
  img.src     = "img/tiles_32x32.png";
  var _draw   = this._draw
  img.onload  = function () {
      _draw.drawImage(img, 50, 300);
  }
  // this._draw.drawImage(img, 50, 300);
}

Erutazero.game.init = function() {
  this._canvas  = Beja.id('canvas');
  this._draw    = null;
  if (this._canvas && this._canvas.getContext) {
  //check whether browser support getting canvas context
    this._draw = this._canvas.getContext('2d');
  } else {
    throw("Canvas not supported");
  } 
  this.draw_test();  
}  

Erutazero.Game = Beja.class(Erutazero.game);

/*
Beja.Base = function() {};
Beja.Base.Methods = function() {};
Beja.Base.Methods.
Beja.Base.prototype = Beja.Object.Methods;
*/

FooClass        = Beja.class(null)
FooClass.init   = function() { Beja.puts("Init ok!"); }
FooClass.foo    = function() { Beja.puts("foo!"); }
FooClass._var   = "variable"
Foo             = Beja.class(FooClass);
BarMethods      = {};
BarMethods.bar  = function() { Beja.puts("Bar!!!" + this._var); },
BarMethods.init = function(inp) { Beja.puts("Init bar ok!: " + inp); } 
Bar             = Beja.inherit(Foo, BarMethods);

Bar.prototype.bar = function() { 
  Beja.puts("Bar was overridden!!!" + this._var);
}   
 
 
Beja.onload = function () {
  o = new Bar("Indeed!");
  o.foo();
  o.bar(); 
  game = new Erutazero.Game();
  // game.initialize();
  
}
 
 























