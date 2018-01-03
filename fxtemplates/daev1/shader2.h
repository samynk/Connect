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
// shader2 Class
//-----------------------------------------------------------------
class shader2:public AbstractGame{
	public:
	//---------------------------
	// Constructor(s)
	//---------------------------
	shader2();
	//---------------------------
	// Destructor
	//---------------------------
	virtual ~shader2();
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
	shader2(const shader2& tRef);
	shader2& operator=(const shader2& tRef);
};

