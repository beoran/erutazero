

Erutazero      = Beja.module();
Erutazero.KEY  = { D: 68, W: 87, A: 65, S:83, 
                   RIGHT: 39, UP: 38, LEFT: 37, DOWN: 40, SPACE: 32, Q: 81 };

Erutazero.game = Beja.module();

/** Image preloader */
Erutazero.preloader = Beja.module();
Erutazero.preloader.init = function(names) {
  this.complete  = null;
  this.loaded    = null; 
  this._current  = null;
  this._names    = names; 
  this._inprocess= false;
  this._images   = {}; // hash table
  this._names    = names; // array of names, hopefully
  this._toload   = names.length;
  this._count    = 0;
}

Erutazero.preloader.image = function(name) {
  return this._images[name];
}

Erutazero.preloader.store = function(name, image) {
  return this._images[name] = image;
}


Erutazero.preloader.load_all = function() {
  this._inprocess = true;
  while(this._names.length >0) { 
    this.load_image(this._names.shift());
  }  
  this._inprocess = false;
}
  
Erutazero.preloader.load_image = function(name) {
  var self = this;
  var img  = new Image();
  img.onload = function () {
    self._current = img;
    self.store(name, img);
    self._count  += 1;
    if (self.loaded) self.loaded(name, img);
    if (self._toload == self._count) {
      if(self.complete) self.complete(self);
    }
  }
  img.src = name;
  
}
Erutazero.load_image = function(filename) {
  var done      = false;
  var error     = false;
  var image     = new Image();
  image.onload  = function () { done  = true; }
  image.onerror = function () { error = true; }
  image.onabort = function () { error = true; }
  image.src     = filename;
  /*while (!done)  { 
    if(error) return null;
  }*/
  return image;
}

Erutazero.make_canvas = function(wide, high) { 
  var buffer = document.createElement('canvas');
  buffer.width = wide;
  buffer.height = high;
  return buffer;
};


Erutazero.render_canvas = function(wide, high, render_function) { 
  var buffer   = Erutazero.make_canvas(wide, high);
  render_function(buffer.getContext('2d'));
  return buffer;
};

/* A layer in a tile map. It is significantly faster in drawing to buffer 
the tile map layer to a prerendered canvas, although the prerendering may be 
slow for somewhat larger tile maps. */

Erutazero.layer = Beja.module();
Erutazero.layer.init = function (wide, high, tileset) {
  this._tileset     = Erutazero.load_image(tileset)
  this._tilecount   = this._tileset.width * this._tileset.height / (1024)
  this._tileindex   = new Array();  
  this._high        = high;
  this._wide        = wide;
  this._tiles       = new Array();  
  for (var y =0; y < this._high; y++) {
    this._tiles[y]  = new Array();
    for(var x=0; x < this._wide; x++) {
      this._tiles[y][x] = 0;
    }
  }
  // make a buffer and prerender the tile map. 
  this._buffer      = Erutazero.make_canvas(this._wide * 32, this._high * 32)
  this.render(this._buffer.getContext('2d'), 640, 480, 0, 0); 
}

/** Prerenders the tile map. */
Erutazero.layer.render = function(screen, sw, sh, dx, dy) { 
  for (var y =0; y < this._high; y++) {  
    for(var x=0; x < this._wide; x++) {
      itile = this._tiles[y][x]
      // tile  = (itile != null) ? this._images[itile] : null
      if (itile != null) {
        srcx = (itile * 32) % this._tileset.width;
        srcy = (itile * 32) / this._tileset.width;
        dstx = x * 32 + dx;
        dsty = y * 32 + dy;
        // screen.drawImage(tile, dstx, dsty);
        screen.drawImage(this._tileset, srcx, srcy, 32, 32, dstx, dsty, 32, 32);
      }
    }
  }
}

Erutazero.layer.draw = function(screen, sw, sh, dx, dy) { 
  screen.drawImage(this._buffer, dx, dy);
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
  
}


Erutazero.make_request_animate_frame = function() {
  return  window.requestAnimationFrame       || 
          window.webkitRequestAnimationFrame || 
          window.mozRequestAnimationFrame    || 
          window.oRequestAnimationFrame      || 
          window.msRequestAnimationFrame     || 
          function(/* function */ callback, /* DOMElement */ element) {
            window.setTimeout(callback, 1000 / 60);
          };
}

Erutazero.request_animate_frame = function(cb) { 
  var tocall = window.requestAnimationFrame      ||
          window.webkitRequestAnimationFrame ||
          window.mozRequestAnimationFrame    ||
          window.oRequestAnimationFrame      ||
          window.msRequestAnimationFrame     ||
          function(/* function */ callback, /* DOMElement */ element) {
            window.setTimeout(callback, 1000 / 60);
          };
  return tocall(cb);
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
  this._delay    = 1000 / 60; 
  var self       = this;
  this._update   = function() { self.update(); }
  this._busy     = true;
  this._dirty    = true;  
  this._x        = this._y = 0; 
  this._keys     = {};
  this._interval = window.setTimeout(this._update, this._delay);
  //this._interval = window.mozRequestAnimationFrame(this._update);
  // Beja.puts(Erutazero.request_animate_frame)
  // Erutazero.request_animate_frame.call(this._update);
  // window.setInterval(this._update, this._delay);
  this._draw.font= "12px serif";  
  this._layer    = new Erutazero.Layer(200, 200, "img/tiles_1000_village.png");
  this._onkeydown= function(ev) { return self.onkeydown(ev) }
  this._onkeyup  = function(ev) { return self.onkeyup(ev)   }
  Beja.Event.listen(window, "keydown", this._onkeydown);
  Beja.Event.listen(window, "keyup"  , this._onkeyup); 
}  

Erutazero.game.onkeydown = function(event) { 
  this._x                   = this._x - 1;
  this._y                   = this._y - 1;
  this._dirty               = true;
  // this._keys[event.keyCode] = true;
  // switch (event.keyCode) { }
  return false; 
}

Erutazero.game.onkeyup = function(event) { 
  // this._keys[event.keyCode] = false;
  // switch (event.keyCode) { }
  return false; 
}


Erutazero.game.draw = function() {
  if (!this._dirty) return;
  this._layer.draw(this._draw, this._canvas.width, this._canvas.height, this._x, this._y);
  this._draw.fillText("(" + this._x + "," + this._y + ")", 20, 20);
  // this._draw.fillText("(" + this._keys[Erutazero.KEY.UP] + ")", 20, 40);  
  this._dirty = false;
}

Erutazero.game.update = function() {
  this._busy = (this._x > -1000); 
  if (this._busy) {  
    this._interval = window.setTimeout(this._update, this._delay);
  }
  this.draw();
  // this._interval = Erutazero.request_animate_frame(this._update);
  // this._interval = window.mozRequestAnimationFrame(this._update);
  // RequestAnimationFrame is not really that much smoother...
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
 
 























