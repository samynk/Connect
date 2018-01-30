//-----------------------------------------------------------------
// Game File
// C++ Source - DAEGame1.cpp - version v2_14 sept 2014 
// Copyright DAE Programming Team
// http://www.digitalartsandentertainment.be/
//-----------------------------------------------------------------

//-----------------------------------------------------------------
// Include Files
//-----------------------------------------------------------------
#include "<%=node.name%>.h"																				

//-----------------------------------------------------------------
// Defines
//-----------------------------------------------------------------
#define GAME_ENGINE (GameEngine::GetSingleton())

//-----------------------------------------------------------------
// DAEGame1 methods																				
//-----------------------------------------------------------------

<%=node.name%>::<%=node.name%>()
{
	// nothing to create
}

<%=node.name%>::~<%=node.name%>()																						
{
	// nothing to destroy
}

void <%=node.name%>::GameInitialize(GameSettings &gameSettings)
{
	gameSettings.SetWindowTitle("<%=node.name%> - Name, First name - group"));
	gameSettings.SetWindowWidth(842);
	gameSettings.SetWindowHeight(480);
	gameSettings.EnableConsole(false);
	gameSettings.EnableVSync(true);
	gameSettings.EnableAntiAliasing(false);
}