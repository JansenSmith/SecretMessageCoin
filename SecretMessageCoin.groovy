// by Jansen Smith 

import eu.mihosoft.vrl.v3d.svg.*
import eu.mihosoft.vrl.v3d.*
import eu.mihosoft.vrl.v3d.CSG
import eu.mihosoft.vrl.v3d.Extrude
import eu.mihosoft.vrl.v3d.Polygon
import com.neuronrobotics.bowlerstudio.scripting.ScriptingEngine

def border_y = 4

def suit_z = 0.2
def suit_space_y = 0.5
def suit_y = border_y - suit_space_y*2

def channel_depth_z = 0.5
def channel_width_y = 1

CSG coin = new Cylinder(39/2, 39/2, 3.5, (int)128).toCSG() // standard poker chip size from https://www.poker.org/poker-chip-values/
				.moveToCenterZ()

double channel_rad = coin.maxY - border_y
CSG channel_inner = new Cylinder(channel_rad-channel_width_y, channel_rad-channel_width_y, channel_depth_z, (int)128).toCSG()
CSG channel = new Cylinder(channel_rad, channel_rad, channel_depth_z, (int)128).toCSG()
					.difference(channel_inner)
					.movez(coin.minZ)
channel = channel.union(channel.mirrorz()) 
coin = coin.difference(channel)

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
CSG suit_heart = s.extrudeLayerToCSG(suit_z,"heart")
CSG suit_spade = s.extrudeLayerToCSG(suit_z,"spade")
CSG suit_diamond = s.extrudeLayerToCSG(suit_z,"diamond")
CSG suit_club = s.extrudeLayerToCSG(suit_z,"club")
def suit_scale_factor = suit_y/suit_heart.totalY
suit_heart = suit_heart.scalex(suit_scale_factor)
					.scaley(suit_scale_factor)
					.toYMax()
					.toZMax()
					.movez(coin.maxZ)
					.movey(coin.maxY - suit_space_y)
suit_spade = suit_spade.scalex(suit_scale_factor)
					.scaley(suit_scale_factor)
					.toYMax()
					.toZMax()
					.movez(coin.maxZ)
					.movey(coin.maxY - suit_space_y)
					.rotz(90)
suit_diamond = suit_diamond.scalex(suit_scale_factor)
					.scaley(suit_scale_factor)
					.toYMax()
					.toZMax()
					.movez(coin.maxZ)
					.movey(coin.maxY - suit_space_y)
					.rotz(180)
suit_club = suit_club.scalex(suit_scale_factor)
					.scaley(suit_scale_factor)
					.toYMax()
					.toZMax()
					.movez(coin.maxZ)
					.movey(coin.maxY - suit_space_y)
					.rotz(270)
					
CSG suit_red = suit_heart.union(suit_diamond)
suit_red = suit_red.union(suit_red.mirrorz())
				.toZMin()
CSG suit_black = suit_spade.union(suit_club)
suit_black = suit_black.union(suit_black.mirrorz())
				.toZMin()

coin = coin.toZMin()
				.difference(suit_red, suit_black)

////////

coin = coin.setColor(javafx.scene.paint.Color.DARKGRAY)
			.setName("coin")
			.addAssemblyStep(0, new Transform())
			.setManufacturing({ toMfg ->
				return toMfg
						//.rotx(180)// fix the orientation
						//.toZMin()//move it down to the flat surface
			})

return [coin, suit_red, suit_black]
//return suit_heart

