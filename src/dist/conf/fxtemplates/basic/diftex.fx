//-----------------------------------------------------------------
// Game File
// C++ Source - DAEGame1.cpp - version v2_14 sept 2014
// Copyright DAE Programming Team
// http://www.digitalartsandentertainment.be/
//-----------------------------------------------------------------
//-----------------------------------------------------------------
// Include Files
//-----------------------------------------------------------------
#include "diftex.h"
//-----------------------------------------------------------------
// Defines
//-----------------------------------------------------------------
#define GAME_ENGINE (GameEngine::GetSingleton())
//-----------------------------------------------------------------
// DAEGame1 methods
//-----------------------------------------------------------------
diftex::diftex()
{
	// nothing to create
}

diftex::~diftex()
{
	// nothing to destroy
}

void diftex::GameInitialize(GameSettings &gameSettings)
{
	gameSettings.SetWindowTitle("diftex - Name, First name - group"));
	gameSettings.SetWindowWidth(842);
	gameSettings.SetWindowHeight(480);
	gameSettings.EnableConsole(false);
	gameSettings.EnableVSync(true);
	gameSettings.EnableAntiAliasing(false);
}

void diftex::GameStart(){
	gDifTex = new Bitmap(<!Value.bitmapnot found>);
}

void diftex::GameEnd(){
	delete gDifTex;
	gDifTex = nullptr;
}

struct VS_INPUT{
	float3 iPosL : POSITION;
	float2 iTexCoord : TEXCOORD;
};

struct VS_OUTPUT{
	float4 oPosH : SV_POSITION;
	float2 oTex : TEXCOORD;
};

VS_OUTPUT  VS(VS_INPUT input) {
	VS_OUTPUT output= (VS_OUTPUT)0;
	// Transforming as position
	float4 mtx1_tvec4 = mul ( float4(input.iPosL,1.0f) , mtxWorldVP );
	//constructing output
	output.oPosH=mtx1_tvec4;
	output.oTex=input.iTexCoord;
	return output;
}

struct PS_INPUT{
	float4 posH : SV_POSITION;
	float2 tc : TEXCOORD;
};

float4  PS(PS_INPUT input)  : SV_TARGET {
	// should be included in gametick.
	//constructing output
	return gDifTex_RGBA;
}

