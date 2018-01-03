// Game File
// C++ Source - DAEGame1.h - version v2_14 sept 2014
// Copyright DAE Programming Team
// http://www.digitalartsandentertainment.be/
//-----------------------------------------------------------------
//-----------------------------------------------------------------
// Student data
// Name:
// Group:
//-----------------------------------------------------------------
#pragma once
//-----------------------------------------------------------------
// Include Files
//-----------------------------------------------------------------
#include "Resource.h"
#include "GameEngine.h"
#include "AbstractGame.h"
//-----------------------------------------------------------------
// diftex Class
//-----------------------------------------------------------------
class diftex:public AbstractGame{
	public:
	//---------------------------
	// Constructor(s)
	//---------------------------
	diftex();
	//---------------------------
	// Destructor
	//---------------------------
	virtual ~diftex();
	//---------------------------
	// General Methods
	//---------------------------
	void GameInitialize(GameSettings &gameSettings);
	void GameStart();
	void GameEnd();
	void GameTick(double deltaTime);
	void GamePaint(RECT rect);
	// -------------------------
	// Member functions
	// -------------------------
	private:
	Bitmap * gDifTex;
	diftex(const diftex& tRef);
	diftex& operator=(const diftex& tRef);
};

