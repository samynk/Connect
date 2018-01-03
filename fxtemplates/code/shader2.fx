cbuffer cbPerObject{
matrix mtx1 : World;
matrix mtxWorldVP : WorldViewProjection;

};

cbuffer cbPerFrame{

};

float3 light1 : Direction <  
string UIName = "Light Direction"; 
string Object = "TargetLight";
> = float3(-0.577, -0.577, 0.577);
float4 c1<
string UIName = "Diffuse Color";
string UIWidget = "Color";
> = float4 (0.003921569,0.003921569,0.0039215,1.0);
float f1< 
string UIName = "Diffuse Intensity";
string UIType = "FloatSpinner";
float UIMin = 0.0f;
float UIMax = 1.0f;
>
=0.0;

struct VS_INPUT{
float3 iPosL : POSITION;
float3 iNormal : NORMAL;
};
struct VS_OUTPUT{
float4 oPosH : SV_POSITION;
float3 oNormal : NORMAL;
};

VS_OUTPUT VS(VS_INPUT input){
VS_OUTPUT output;

float3 code2;
float3x3 worldRot = (float3x3)mtx1;
code2 = mul(input.iNormal,worldRot);
code2 = normalize(code2);

float4 code1;
float4 toTransform = float4(input.iPosL,1);
code1 = mul(toTransform,mtxWorldVP);
output.oPosH=code1;
output.oNormal=code2;
return output;
}
struct PS_INPUT{
float4 posH : SV_POSITION;
float3 iNormal : NORMAL;
};
struct PS_OUTPUT{
float4 color0 : SV_TARGET;
};

PS_OUTPUT PS(PS_INPUT input){
PS_OUTPUT output;

float3 c1_rgb=c1.rgb;


float4 code1;
float3 colorToUse = f1 * c1_rgb;
// diffuse calculation
float lightAmount = dot ( light1, input.iNormal );
// make sure amount is not negative.
lightAmount = max ( lightAmount, 0 ); 
colorToUse = lightAmount * colorToUse;
// make sure rgb values are between 0 and 1
colorToUse = saturate( colorToUse); 
code1 = float4 ( colorToUse, 1);
output.color0=code1;
return output;
}
// dit is specifiek voor DirectX10
technique10 basicshader{

pass one{
SetVertexShader( CompileShader ( vs_4_0, VS() ));
SetPixelShader( CompileShader ( ps_4_0, PS() ));
SetGeometryShader(NULL);

}

}