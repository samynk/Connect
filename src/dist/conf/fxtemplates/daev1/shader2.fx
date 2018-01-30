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
shader2::Paint(RECT rect )
{
	DOUBLE2 d2(0.0,0.0);
	DOUBLE2 d1(0.0,0.0);
	math1 = length( d1 );
	GAME_ENGINE->DrawEllipse( math1,d2.x,d2.y);
	//constructing output
}

