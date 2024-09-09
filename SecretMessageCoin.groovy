// by Jansen Smith 

import eu.mihosoft.vrl.v3d.svg.*
import eu.mihosoft.vrl.v3d.CSG
import eu.mihosoft.vrl.v3d.Extrude
import eu.mihosoft.vrl.v3d.Polygon
import com.neuronrobotics.bowlerstudio.scripting.ScriptingEngine

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
HashMap<String,List<Polygon>> polygonsByLayer = s.toPolygons()
// extrude all layers to a map to 10mm thick
HashMap<String,ArrayList<CSG>> csgByLayers = s.extrudeLayers(10)
// extrude just one layer to 10mm
// The string "heart" represents the layer name in Inkscape
def suit_heart = s.extrudeLayerToCSG(10,"heart")
//// seperate holes and outsides using layers to differentiate
//def outsideParts = s.extrudeLayerToCSG(10,"spade")
//					.difference(holeParts)
//// layers can be extruded at different depths
//def boarderParts = s.extrudeLayerToCSG(5,"3-boarder")

return suit_heart


