// by Jansen Smith 

import eu.mihosoft.vrl.v3d.svg.*
import eu.mihosoft.vrl.v3d.*
import eu.mihosoft.vrl.v3d.CSG
import eu.mihosoft.vrl.v3d.Extrude
import eu.mihosoft.vrl.v3d.Polygon
import com.neuronrobotics.bowlerstudio.scripting.ScriptingEngine

def import_suits = 0

def suit_thickness = 0.1

CSG coin = new Cylinder(39, 39, 3.5, (int)128).toCSG() // standard poker chip size from https://www.poker.org/poker-chip-values/

if(import_suits) {
	File f = ScriptingEngine
		.fileFromGit(
			"https://github.com/JansenSmith/SecretMessageCoin.git",//git repo URL
			"main",//branch
			"Anglo-American_card_suits_centered_layered.svg"// File from within the Git repo
		)
	println "Extruding SVG "+f.getAbsolutePath()
	SVGLoad s = new SVGLoad(f.toURI())
	println "Layers= "+s.getLayers()
	// A map of layers to polygons
	//HashMap<String,List<Polygon>> polygonsByLayer = s.toPolygons()
	// extrude all layers to a map to 10mm thick
	//HashMap<String,ArrayList<CSG>> csgByLayers = s.extrudeLayers(suit_thickness)
	// extrude just one layer to 10mm
	// The string "heart" represents the layer name in Inkscape
	def suit_heart = s.extrudeLayerToCSG(suit_thickness,"heart")
	def suit_spade = s.extrudeLayerToCSG(suit_thickness,"spade")
	def suit_diamond = s.extrudeLayerToCSG(suit_thickness,"diamond")
	def suit_club = s.extrudeLayerToCSG(suit_thickness,"club")
	coin = suit_heart
}

return coin


