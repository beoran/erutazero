/*
* Cross browser library based on Universe and other sources, 
* but it attempts to be readable frst, and small second, 
* so it's a bit bigger.  
*/

Beja              = new Object();
Beja.prototype    = Beja;

Beja.Cookie       = new Object();
 
/** Sets a named cookie. */
Beja.Cookie.set   = function (name, value, exdays) {
  var exdate      = new Date();
  var expire      = "";
  if (exdays)  { 
    exdate.setDate(exdate.getDate() + exdays)
    expire        = "; expires=" + exdate.toUTCString();
  }
  var value       = escape(value) + expire;
  document.cookie = name + "=" + value;
}

/** Gets a named cookie. */
Beja.Cookie.get   = function (name) {
  var i,x,y,cookies = document.cookie.split(";");
  for (i=0; i< cookies.length; i++) {
    var eqi = cookies[i].indexOf("=");
    x       = cookies[i].substr(0, eqi);
    y       = cookies[i].substr(eqi + 1);
    x       = x.replace(/^\s+|\s+$/g , "");
    if (x == name) return unescape(y);
  }
  return null;
}


/* Function to get an element on the web page by it's ID. 
Returns ID it'self if it's not a string.  */
Beja.id = function(id) {
  if (typeof id !='string') return id;
  if (document.getElementById) return document.getElementById(id);
  // Modern, DOM-based browsers.
  if(document.all) return document.all[id]; // IE5  
  return eval('document.' + id); // NS4
}

/* Same function, but written shorter */
Beja.$ = function(id) { return Beja.id(id); }

/* A shorter way to write element.getElementsByTagName(name.toUpperCase()) */
Beja.name = function (parent, name) {
  return parent.getElementsByTagName(name.toUpperCase());
}

/* Sets an attribute of the element of the document to a different element. */
Beja.set_attribute = function (myid, key, val) { 
    var el = this.id(myid); 
    if(el)  { el[key] = val;            }
    else    { return false;             } 
    return true; 
}

/* Fills in the innerHTML attribute of an element. May not be portable across platforms. */    
Beja.set_inner_html  = function (myid, val) { 
    Beja.set_attr(myid, 'innerHTML', val) 
}


Beja.Event = new Object();

/** Attaches an event handler. */
Beja.Event.listen = function (object, event, func) { 
  if (window.addEventListener) 
    return object.addEventListener(event,func,false);
  return object.attachEvent("on" + event, func);
}

/** Removes an event handler. */
Beja.Event.ignore = function(object, event, func) { 
  if (window.removeEventListener) 
    return object.removeEventListener(event,func,false);
  return object.detachEvent("on"+event, func);
}

/* 
  Wraps an event handler of the type f(event) and returns a new one that solves 
  the problems with IE. So no more need for if (!e) var e = window.event; this 
  function does it automagically for you.
*/
Beja.Event.wrap = function (handler) {
  return function(e) {
    if (!e) var e = window.event;
    handler(e)
  }
}

/* Determine the element on which the event was invoked. */
Beja.Event.target = function(e) {
  var targ;
	if (e.target) targ = e.target;
	else if (e.srcElement) targ = e.srcElement;
	if (targ.nodeType == 3) // defeat Safari bug
		targ = targ.parentNode;
  return targ;
}

/* Determine the position of the mouse relative to the current document. */
Beja.Event.mouse_at = function (e) {
	var posx = -1;
	var posy = -1;
	if (!e) var e = window.event;
	if (e.pageX || e.pageY) 	{
		posx = e.pageX;
		posy = e.pageY;
	}
	else if (e.clientX || e.clientY) 	{
		posx = e.clientX + document.body.scrollLeft	+ document.documentElement.scrollLeft;
		posy = e.clientY + document.body.scrollTop	+ document.documentElement.scrollTop;
	}
	// posx and posy contain the mouse position relative to the document
	return [posx, posy]
}


Beja.handle_onload = function() {
  if (Beja.onload) return Beja.onload(); 
  return false;
}

/* Copies all the methods in methods to klass.prototype */
Beja.copy_methods_to_prototype = function(klass, methods) {
  if(!methods) return; 
  for (name in methods) {
    klass.prototype[name] = methods[name]; 
  } 
} 

/* Returns a generic constructor function that will call the init method. */
Beja.make_constructor = function() { 
  return function() {
    if(typeof(this['init']) == 'function') {  
      this['init'].apply(this, arguments);
    } 
  };  
}

/* Basic oop. */
Beja.class = function(methods) {
  klass    = Beja.make_constructor();
  Beja.copy_methods_to_prototype(klass, methods);
  return klass; 
}

/* Basic oop, and inheritance. */
Beja.inherit = function(parentklass, methods) {
  klass                       = Beja.make_constructor();
  klass.prototype             = new parentklass();
  klass.prototype.constructor = klass;
  Beja.copy_methods_to_prototype(klass, methods);
  return klass;
};
  
/* Module does not have any inheritance. */
Beja.module = function() {
  return Beja.make_constructor();
}

/* Debug logging. */
Beja.puts = function(str) {
  s = String(str);
  var newdiv = document.createElement('div');
  var out    = Beja.id('debug');
  newdiv.appendChild(document.createTextNode(s));
  // newdiv.className = type;
  if (out) out.appendChild(newdiv); 
  /* else if(console) { console.log(s); }  */  
  return newdiv;
}

Beja.Event.listen(window, "load", Beja.handle_onload);



