#!/usr/bin/env ruby 
# My own bloody build script. Even BUILDR is too SLOOOOOOOOW! Argh! :p
#

require 'pstore'

module Yabar
  DEBUG   = true 
  VERBOSE = true
  SAVE    = false
  class Task
    attr_reader :name
    attr_reader :act
    attr_reader :need
    attr_reader :error
    
    def initialize(name, *need, &act) 
      @name = name.to_sym
      @need = need.map{ |r| r.to_sym }    
      @act  = act
      @ran  = false
      @ok   = false
      @error= nil
    end
    
    def load_run(info)
      if info 
	@ran = true
	@ok  = true
      end
    end
    
    def save_run()
      return @ran && @ok
    end
    
    def run    
      return nil if @ran # don't run twice    
      # Run needed tasks recursively 
      self.need.each do | n |
        t = Yabar.get_task(n) 
        unless t
	  warn "No such task #{n}!"
	  return false 
	end  
	return false unless t.run
      end  
      # Now run self. 
      p "Running #{self.name}"
      if @act
	@ok, @error = @act.call() 
      else
	@ok 	  = true
      end
      unless @ok
	warn "Task #{@name} failed:\n #{@error}"
      end    
      @ran = true
      return @ok
    end
    
  end  
  
  CACHE = 'yab.cache'
  
  def open_cache
    @cache ||= PStore.new(CACHE)
    return @cache
  end
  
  def load_task(name)
    return nil unless SAVE 
    open_cache.transaction do |c|
      return c[name.to_sym]
    end
  end
  
  def save_task(name, info)
    return nil unless SAVE
    open_cache.transaction do |c|
      c[name.to_sym] = info
    end
  end
  
  def get_task(name)
    return @@tasks[name.to_sym]
  end
  
  def add_task(t)
    @@tasks ||= {}    
    @@tasks[t.name.to_sym] = t
    t.load_run(load_task(t.name))     
    return @@tasks
  end
  
  def save_tasks
    @@tasks ||= {}
    @@tasks.each do |k, t| 
      save_task(t.name, t.save_run)
    end
  end
  
  def run(name = :compile) 
    to_run = self.get_task(name)
    res    = to_run.run
    save_tasks
    return res
  end
  
  def autorun
    name = ::ARGV[0] || :compile
    puts "Autoruning task: #{name}" if VERBOSE
    run(name.to_sym) 
  end
  
  def task(name, *need, &act)    
    t = Task.new(name, *need, &act)
    add_task(t)
  end
  
  def shell(command, *args)
    cmd = ([ command ] + args).map{|e| e.to_s}.join(' ')
    puts cmd if VERBOSE
    text = %x(#{cmd})
    return $?.success?, text
  end
  
  extend self
  
end  

extend Yabar
include Yabar
 

JAVAC 		= 'javac -source 1.3 -target 1.1'
JAVA 		= 'java'
JAR 		= 'jar'
RUN_CLASSPATH 	= 'vendor/microemulator-2.0.4/microemulator.jar:vendor/microemulator-2.0.4/lib/midpapi20.jar:vendor/microemulator-2.0.4/lib/cldcapi10.jar'
PRE_JAR_NAME  	= 'ErutaZeroPre.jar'
JAR_NAME  	= 'ErutaZero.jar'
MANIFEST  	= 'build/manifest.mf'
BUILD_DIR 	= 'build/compiled'
BUILD_CLASSPATH = RUN_CLASSPATH
SRC_PATH   	= 'src'
MAIN_FILE 	= 'src/erutazero/game/ErutaZero.java'
MAIN_CLASS      = 'erutazero.game.ErutaZero'
PROGUARD        = 'vendor/proguard4.5.1/bin/proguard.sh'
C3_DIR		= '/media/0000-EDA2'



task :compile do 
  shell JAVAC, '-bootclasspath', BUILD_CLASSPATH, '-sourcepath', SRC_PATH, '-d', BUILD_DIR, MAIN_FILE
end

task :jar, :compile do
  shell JAR, 'cvfm', PRE_JAR_NAME, MANIFEST, '-C',  BUILD_DIR, '.'
end

# Use PROGUARD as a microedition verification tool.
task :verify, :jar do
  shell PROGUARD, '-microedition', '-injars', PRE_JAR_NAME, '-outjars', JAR_NAME, 
        '-libraryjars', BUILD_CLASSPATH, '-dontshrink', '-keepdirectories', 
        '-repackageclasses ', '-allowaccessmodification', '-printseeds',        
        '-keep "public class * extends javax.microedition.midlet.MIDlet"',
        '-adaptresourcefilenames  "**.properties,**.png,**.jpg,**.rak"',
	'-adaptresourcefilecontents "**.properties,META-INF/MANIFEST.MF"'

        
# -overloadaggressively
# -microedition

=begin
~/arch/dl/mobile/proguard4.5.1/bin/proguard.sh -injars ErutaZeroPre.jar -libraryjars vendor/microemulator-2.0.4/microemulator.jar:vendor/microemulator-2.0.4/lib/midpapi20.jar:vendor/microemulator-2.0.4/lib/cldcapi10.jar  -outjars ErutaZero.jar -dontshrink -keepdirectories   -keep class erutazero.game.ErutaZero
=end
end

task :run, :verify do 
  shell JAVA, '-cp', RUN_CLASSPATH, 'org.microemu.app.Main', 'ErutaZero.jar'
end  

task :genkey do 
#  keytool -genkey -alias erutazero-key -keystore build/keystore
end

task :sign do
#  jarsigner -keystore build/keystore -storepass storekey -keypass storekey ErutaZero.jar erutazero-key
end

task :c3, :verify do
  shell 'cp', JAR_NAME, C3_DIR  
  shell 'umount', C3_DIR
end

Yabar.autorun 

