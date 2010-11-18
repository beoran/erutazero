def calcol(lum, prop, third = 15) 
 return (lum - third) / (prop + 1) 
end

def nicesum(s)
 res = []; (1..s).each { |i| o = (s-i) ; rat = (s+0.0) / i ; res << [i, o, rat] if rat * 10 == (rat*10).round } 
 return res
end
