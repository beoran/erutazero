

Erutazero      = Beja.module();
Erutazero.game = Beja.module();

Erutazero.layer = Beja.module();
Erutazero.layer.init = function (wide, high, tileset) {
  this._tileset     = new Image();
  this._tileset.src = tileset;
  this._high        = high;
  this._wide        = wide;
  this._tiles       = new Array();  
  for (var y =0; y < this._high; y++) {
    this._tiles[y] = new Array();
    for(var x=0; x < this._wide; x++) {
      this._tiles[y][x] = 0;
    }
  }
}

Erutazero.layer.draw = function (screen, sw, sh, dx, dy) { 
  for (var y =0; y < this._high; y++) {  
    for(var x=0; x < this._wide; x++) {
      tile = this._tiles[y][x]
      if (tile != null) {
        srcx = (tile * 32) % this._tileset.width;
        srcy = (tile * 32) / this._tileset.width;
        dstx = x * 32;
        dsty = y * 32;
        screen.drawImage(this._tileset, srcx, srcy, 32, 32, dstx, dsty, 32, 32)
      }
    }
  }
}

Erutazero.Layer = Beja.class(Erutazero.layer);

Erutazero.game.draw_test = function() {
  this._draw.fillStyle = "rgb(127,0,0)";
  this._draw.fillRect(10, 20, 100, 85);
  
  this._draw.strokeStyle = "rgb(0,0,0)";
  this._draw.strokeRect(0, 0, 640, 480);
  this._draw.fillStyle = "rgb(127,255,0)";
  this._draw.font = "bold 26px serif";
  this._draw.fillText('Test text OK!', 10, 240);

  var layer   = new Erutazero.Layer(20, 15, "img/tiles_1000_village.png");
  
  var _draw   = this._draw
  var _canvas = this._canvas
  layer._tileset.onload  = function () {
      _draw.drawImage(layer._tileset, 50, 300);
      
      start = (new Date()).getTime();
      layer.draw(_draw, _canvas.width, _canvas.height, 0, 0); 
      stop  = (new Date()).getTime();
      delta = stop - start;
      Beja.puts("Time for one layer draw: " + delta);
      // around 25 fps.. quite low :p
  }
  
  
  
  /*
  var img     = new Image();
  img.src     = ;
  */
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
 
 























