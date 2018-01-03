//-----------------------------------------------------------------
// Game File
// C++ Source - DAEGame1.cpp - version v2_14 sept 2014
// Copyright DAE Programming Team
// http://www.digitalartsandentertainment.be/
//-----------------------------------------------------------------
//-----------------------------------------------------------------
// Include Files
//-----------------------------------------------------------------
#include "shader2.h"
//-----------------------------------------------------------------
// Defines
//-----------------------------------------------------------------
#define GAME_ENGINE (GameEngine::GetSingleton())
//-----------------------------------------------------------------
// DAEGame1 methods
//-----------------------------------------------------------------
shader2::shader2()
{
	// nothing to create
}

shader2::~shader2()
{
	// nothing to destroy
}

void shader2::GameInitialize(GameSettings &gameSettings)
{
	gameSettings.SetWindowTitle("shader2 - Name, First name - group"));
	gameSettings.SetWindowWidth(842);
	gameSettings.SetWindowHeight(480);
	gameSettings.EnableConsole(false);
	gameSettings.EnableVSync(true);
	gameSettings.EnableAntiAliasing(false);
}

void shader2::GameStart(){
}

void shader2::GameEnd(){
}

void
shader2::GameTick(float deltaTime )
{
	//constructing output
}

void
shader2::GamePaint(RECT rect )
{
	GAME_ENGINE->DrawEllipse( ellipse1_positionnotfound,ellipse1_dimensionnotfound.x,ellipse1_dimensionnotfound.y);
	//constructing output
}

