import{aT as X,aU as H,aV as z,aW as T,aX as ne,aY as Z,aD as D,d as R,D as i,O as w,a as $,aG as re,aZ as ae,N as ct,S as Oe,am as Le,a_ as ie,a$ as Se,b0 as _e,b1 as Ue,b2 as dt,A as ee,z as v,b3 as ht,B as U,C as Pe,T as pt,w as ft,b4 as gt,aB as mt,E as vt,H as je,b5 as bt,J as xt,L as ye,a5 as kt,aL as Ie,M as wt,b6 as yt,b7 as Ct,ap as de,a2 as he,aF as St,j as _t,b as Fe,o as Ce,l as Be,e as Ut,k as He,X as $t,Y as At,g as pe,$ as Te,F as Ne,Z as Rt,c as Vt}from"./index-D_-DzWhp.js";import{_ as Mt}from"./setting-item.vue_vue_type_script_setup_true_lang-CLZpVzAL.js";import{_ as Dt,u as zt}from"./Input-Bb-Nkj9P.js";import{_ as Pt}from"./InputGroup-DmCFmhCX.js";import{B as It,V as Ft,a as Bt,u as $e}from"./Follower-CPJzzLbY.js";import{u as qe}from"./use-merged-state-DuV1Lu3L.js";import{_ as Ht}from"./Divider-B3MX9R6D.js";function Xe(e,t,r){t/=100,r/=100;const n=t*Math.min(r,1-r)+r;return[e,n?(2-2*r/n)*100:0,n*100]}function me(e,t,r){t/=100,r/=100;const n=r-r*t/2,a=Math.min(n,1-n);return[e,a?(r-n)/a*100:0,n*100]}function B(e,t,r){t/=100,r/=100;let n=(a,l=(a+e/60)%6)=>r-r*t*Math.max(Math.min(l,4-l,1),0);return[n(5)*255,n(3)*255,n(1)*255]}function Ae(e,t,r){e/=255,t/=255,r/=255;let n=Math.max(e,t,r),a=n-Math.min(e,t,r),l=a&&(n==e?(t-r)/a:n==t?2+(r-e)/a:4+(e-t)/a);return[60*(l<0?l+6:l),n&&a/n*100,n*100]}function Re(e,t,r){e/=255,t/=255,r/=255;let n=Math.max(e,t,r),a=n-Math.min(e,t,r),l=1-Math.abs(n+n-a-1),s=a&&(n==e?(t-r)/a:n==t?2+(r-e)/a:4+(e-t)/a);return[60*(s<0?s+6:s),l?a/l*100:0,(n+n-a)*50]}function Ve(e,t,r){t/=100,r/=100;let n=t*Math.min(r,1-r),a=(l,s=(l+e/30)%12)=>r-n*Math.max(Math.min(s-3,9-s,1),-1);return[a(0)*255,a(8)*255,a(4)*255]}function Tt(e,t){switch(e[0]){case"hex":return t?"#000000FF":"#000000";case"rgb":return t?"rgba(0, 0, 0, 1)":"rgb(0, 0, 0)";case"hsl":return t?"hsla(0, 0%, 0%, 1)":"hsl(0, 0%, 0%)";case"hsv":return t?"hsva(0, 0%, 0%, 1)":"hsv(0, 0%, 0%)"}return"#000000"}function se(e){return e===null?null:/^ *#/.test(e)?"hex":e.includes("rgb")?"rgb":e.includes("hsl")?"hsl":e.includes("hsv")?"hsv":null}function Nt(e){return e=Math.round(e),e>=360?359:e<0?0:e}function qt(e){return e=Math.round(e*100)/100,e>1?1:e<0?0:e}const Et={rgb:{hex(e){return T(D(e))},hsl(e){const[t,r,n,a]=D(e);return H([...Re(t,r,n),a])},hsv(e){const[t,r,n,a]=D(e);return Z([...Ae(t,r,n),a])}},hex:{rgb(e){return z(D(e))},hsl(e){const[t,r,n,a]=D(e);return H([...Re(t,r,n),a])},hsv(e){const[t,r,n,a]=D(e);return Z([...Ae(t,r,n),a])}},hsl:{hex(e){const[t,r,n,a]=ne(e);return T([...Ve(t,r,n),a])},rgb(e){const[t,r,n,a]=ne(e);return z([...Ve(t,r,n),a])},hsv(e){const[t,r,n,a]=ne(e);return Z([...Xe(t,r,n),a])}},hsv:{hex(e){const[t,r,n,a]=X(e);return T([...B(t,r,n),a])},rgb(e){const[t,r,n,a]=X(e);return z([...B(t,r,n),a])},hsl(e){const[t,r,n,a]=X(e);return H([...me(t,r,n),a])}}};function Ze(e,t,r){return r=r||se(e),r?r===t?e:Et[r][t](e):null}const le="12px",Ot=12,L="6px",Lt=R({name:"AlphaSlider",props:{clsPrefix:{type:String,required:!0},rgba:{type:Array,default:null},alpha:{type:Number,default:0},onUpdateAlpha:{type:Function,required:!0},onComplete:Function},setup(e){const t=w(null);function r(l){!t.value||!e.rgba||(re("mousemove",document,n),re("mouseup",document,a),n(l))}function n(l){const{value:s}=t;if(!s)return;const{width:u,left:p}=s.getBoundingClientRect(),y=(l.clientX-p)/(u-Ot);e.onUpdateAlpha(qt(y))}function a(){var l;ae("mousemove",document,n),ae("mouseup",document,a),(l=e.onComplete)===null||l===void 0||l.call(e)}return{railRef:t,railBackgroundImage:$(()=>{const{rgba:l}=e;return l?`linear-gradient(to right, rgba(${l[0]}, ${l[1]}, ${l[2]}, 0) 0%, rgba(${l[0]}, ${l[1]}, ${l[2]}, 1) 100%)`:""}),handleMouseDown:r}},render(){const{clsPrefix:e}=this;return i("div",{class:`${e}-color-picker-slider`,ref:"railRef",style:{height:le,borderRadius:L},onMousedown:this.handleMouseDown},i("div",{style:{borderRadius:L,position:"absolute",left:0,right:0,top:0,bottom:0,overflow:"hidden"}},i("div",{class:`${e}-color-picker-checkboard`}),i("div",{class:`${e}-color-picker-slider__image`,style:{backgroundImage:this.railBackgroundImage}})),this.rgba&&i("div",{style:{position:"absolute",left:L,right:L,top:0,bottom:0}},i("div",{class:`${e}-color-picker-handle`,style:{left:`calc(${this.alpha*100}% - ${L})`,borderRadius:L,width:le,height:le}},i("div",{class:`${e}-color-picker-handle__fill`,style:{backgroundColor:z(this.rgba),borderRadius:L,width:le,height:le}}))))}}),Me=ct("n-color-picker");function jt(e){return/^\d{1,3}\.?\d*$/.test(e.trim())?Math.max(0,Math.min(Number.parseInt(e),255)):!1}function Xt(e){return/^\d{1,3}\.?\d*$/.test(e.trim())?Math.max(0,Math.min(Number.parseInt(e),360)):!1}function Zt(e){return/^\d{1,3}\.?\d*$/.test(e.trim())?Math.max(0,Math.min(Number.parseInt(e),100)):!1}function Kt(e){const t=e.trim();return/^#[0-9a-fA-F]+$/.test(t)?[4,5,7,9].includes(t.length):!1}function Gt(e){return/^\d{1,3}\.?\d*%$/.test(e.trim())?Math.max(0,Math.min(Number.parseInt(e)/100,100)):!1}const Yt={paddingSmall:"0 4px"},Ee=R({name:"ColorInputUnit",props:{label:{type:String,required:!0},value:{type:[Number,String],default:null},showAlpha:Boolean,onUpdateValue:{type:Function,required:!0}},setup(e){const t=w(""),{themeRef:r}=Oe(Me,null);Le(()=>{t.value=n()});function n(){const{value:s}=e;if(s===null)return"";const{label:u}=e;return u==="HEX"?s:u==="A"?`${Math.floor(s*100)}%`:String(Math.floor(s))}function a(s){t.value=s}function l(s){let u,p;switch(e.label){case"HEX":p=Kt(s),p&&e.onUpdateValue(s),t.value=n();break;case"H":u=Xt(s),u===!1?t.value=n():e.onUpdateValue(u);break;case"S":case"L":case"V":u=Zt(s),u===!1?t.value=n():e.onUpdateValue(u);break;case"A":u=Gt(s),u===!1?t.value=n():e.onUpdateValue(u);break;case"R":case"G":case"B":u=jt(s),u===!1?t.value=n():e.onUpdateValue(u);break}}return{mergedTheme:r,inputValue:t,handleInputChange:l,handleInputUpdateValue:a}},render(){const{mergedTheme:e}=this;return i(Dt,{size:"small",placeholder:this.label,theme:e.peers.Input,themeOverrides:e.peerOverrides.Input,builtinThemeOverrides:Yt,value:this.inputValue,onUpdateValue:this.handleInputUpdateValue,onChange:this.handleInputChange,style:this.label==="A"?"flex-grow: 1.25;":""})}}),Jt=R({name:"ColorInput",props:{clsPrefix:{type:String,required:!0},mode:{type:String,required:!0},modes:{type:Array,required:!0},showAlpha:{type:Boolean,required:!0},value:{type:String,default:null},valueArr:{type:Array,default:null},onUpdateValue:{type:Function,required:!0},onUpdateMode:{type:Function,required:!0}},setup(e){return{handleUnitUpdateValue(t,r){const{showAlpha:n}=e;if(e.mode==="hex"){e.onUpdateValue((n?T:ie)(r));return}let a;switch(e.valueArr===null?a=[0,0,0,0]:a=Array.from(e.valueArr),e.mode){case"hsv":a[t]=r,e.onUpdateValue((n?Z:Ue)(a));break;case"rgb":a[t]=r,e.onUpdateValue((n?z:_e)(a));break;case"hsl":a[t]=r,e.onUpdateValue((n?H:Se)(a));break}}}},render(){const{clsPrefix:e,modes:t}=this;return i("div",{class:`${e}-color-picker-input`},i("div",{class:`${e}-color-picker-input__mode`,onClick:this.onUpdateMode,style:{cursor:t.length===1?"":"pointer"}},this.mode.toUpperCase()+(this.showAlpha?"A":"")),i(Pt,null,{default:()=>{const{mode:r,valueArr:n,showAlpha:a}=this;if(r==="hex"){let l=null;try{l=n===null?null:(a?T:ie)(n)}catch{}return i(Ee,{label:"HEX",showAlpha:a,value:l,onUpdateValue:s=>{this.handleUnitUpdateValue(0,s)}})}return(r+(a?"a":"")).split("").map((l,s)=>i(Ee,{label:l.toUpperCase(),value:n===null?null:n[s],onUpdateValue:u=>{this.handleUnitUpdateValue(s,u)}}))}}))}});function Wt(e,t){if(t==="hsv"){const[r,n,a,l]=X(e);return z([...B(r,n,a),l])}return e}function Qt(e){const t=document.createElement("canvas").getContext("2d");return t?(t.fillStyle=e,t.fillStyle):"#000000"}const en=R({name:"ColorPickerSwatches",props:{clsPrefix:{type:String,required:!0},mode:{type:String,required:!0},swatches:{type:Array,required:!0},onUpdateColor:{type:Function,required:!0}},setup(e){const t=$(()=>e.swatches.map(l=>{const s=se(l);return{value:l,mode:s,legalValue:Wt(l,s)}}));function r(l){const{mode:s}=e;let{value:u,mode:p}=l;return p||(p="hex",/^[a-zA-Z]+$/.test(u)?u=Qt(u):(dt("color-picker",`color ${u} in swatches is invalid.`),u="#000000")),p===s?u:Ze(u,s,p)}function n(l){e.onUpdateColor(r(l))}function a(l,s){l.key==="Enter"&&n(s)}return{parsedSwatchesRef:t,handleSwatchSelect:n,handleSwatchKeyDown:a}},render(){const{clsPrefix:e}=this;return i("div",{class:`${e}-color-picker-swatches`},this.parsedSwatchesRef.map(t=>i("div",{class:`${e}-color-picker-swatch`,tabindex:0,onClick:()=>{this.handleSwatchSelect(t)},onKeydown:r=>{this.handleSwatchKeyDown(r,t)}},i("div",{class:`${e}-color-picker-swatch__fill`,style:{background:t.legalValue}}))))}}),tn=R({name:"ColorPickerTrigger",slots:Object,props:{clsPrefix:{type:String,required:!0},value:{type:String,default:null},hsla:{type:Array,default:null},disabled:Boolean,onClick:Function},setup(e){const{colorPickerSlots:t,renderLabelRef:r}=Oe(Me,null);return()=>{const{hsla:n,value:a,clsPrefix:l,onClick:s,disabled:u}=e,p=t.label||r.value;return i("div",{class:[`${l}-color-picker-trigger`,u&&`${l}-color-picker-trigger--disabled`],onClick:u?void 0:s},i("div",{class:`${l}-color-picker-trigger__fill`},i("div",{class:`${l}-color-picker-checkboard`}),i("div",{style:{position:"absolute",left:0,right:0,top:0,bottom:0,backgroundColor:n?H(n):""}}),a&&n?i("div",{class:`${l}-color-picker-trigger__value`,style:{color:n[2]>50||n[3]<.5?"black":"white"}},p?p(a):a):null))}}}),nn=R({name:"ColorPreview",props:{clsPrefix:{type:String,required:!0},mode:{type:String,required:!0},color:{type:String,default:null,validator:e=>{const t=se(e);return!!(!e||t&&t!=="hsv")}},onUpdateColor:{type:Function,required:!0}},setup(e){function t(r){var n;const a=r.target.value;(n=e.onUpdateColor)===null||n===void 0||n.call(e,Ze(a.toUpperCase(),e.mode,"hex")),r.stopPropagation()}return{handleChange:t}},render(){const{clsPrefix:e}=this;return i("div",{class:`${e}-color-picker-preview__preview`},i("span",{class:`${e}-color-picker-preview__fill`,style:{background:this.color||"#000000"}}),i("input",{class:`${e}-color-picker-preview__input`,type:"color",value:this.color,onChange:this.handleChange}))}}),te="12px",rn=12,j="6px",an=6,on="linear-gradient(90deg,red,#ff0 16.66%,#0f0 33.33%,#0ff 50%,#00f 66.66%,#f0f 83.33%,red)",ln=R({name:"HueSlider",props:{clsPrefix:{type:String,required:!0},hue:{type:Number,required:!0},onUpdateHue:{type:Function,required:!0},onComplete:Function},setup(e){const t=w(null);function r(l){t.value&&(re("mousemove",document,n),re("mouseup",document,a),n(l))}function n(l){const{value:s}=t;if(!s)return;const{width:u,left:p}=s.getBoundingClientRect(),y=Nt((l.clientX-p-an)/(u-rn)*360);e.onUpdateHue(y)}function a(){var l;ae("mousemove",document,n),ae("mouseup",document,a),(l=e.onComplete)===null||l===void 0||l.call(e)}return{railRef:t,handleMouseDown:r}},render(){const{clsPrefix:e}=this;return i("div",{class:`${e}-color-picker-slider`,style:{height:te,borderRadius:j}},i("div",{ref:"railRef",style:{boxShadow:"inset 0 0 2px 0 rgba(0, 0, 0, .24)",boxSizing:"border-box",backgroundImage:on,height:te,borderRadius:j,position:"relative"},onMousedown:this.handleMouseDown},i("div",{style:{position:"absolute",left:j,right:j,top:0,bottom:0}},i("div",{class:`${e}-color-picker-handle`,style:{left:`calc((${this.hue}%) / 359 * 100 - ${j})`,borderRadius:j,width:te,height:te}},i("div",{class:`${e}-color-picker-handle__fill`,style:{backgroundColor:`hsl(${this.hue}, 100%, 50%)`,borderRadius:j,width:te,height:te}})))))}}),fe="12px",ge="6px",sn=R({name:"Pallete",props:{clsPrefix:{type:String,required:!0},rgba:{type:Array,default:null},displayedHue:{type:Number,required:!0},displayedSv:{type:Array,required:!0},onUpdateSV:{type:Function,required:!0},onComplete:Function},setup(e){const t=w(null);function r(l){t.value&&(re("mousemove",document,n),re("mouseup",document,a),n(l))}function n(l){const{value:s}=t;if(!s)return;const{width:u,height:p,left:y,bottom:N}=s.getBoundingClientRect(),q=(N-l.clientY)/p,K=(l.clientX-y)/u,ue=100*(K>1?1:K<0?0:K),G=100*(q>1?1:q<0?0:q);e.onUpdateSV(ue,G)}function a(){var l;ae("mousemove",document,n),ae("mouseup",document,a),(l=e.onComplete)===null||l===void 0||l.call(e)}return{palleteRef:t,handleColor:$(()=>{const{rgba:l}=e;return l?`rgb(${l[0]}, ${l[1]}, ${l[2]})`:""}),handleMouseDown:r}},render(){const{clsPrefix:e}=this;return i("div",{class:`${e}-color-picker-pallete`,onMousedown:this.handleMouseDown,ref:"palleteRef"},i("div",{class:`${e}-color-picker-pallete__layer`,style:{backgroundImage:`linear-gradient(90deg, white, hsl(${this.displayedHue}, 100%, 50%))`}}),i("div",{class:`${e}-color-picker-pallete__layer ${e}-color-picker-pallete__layer--shadowed`,style:{backgroundImage:"linear-gradient(180deg, rgba(0, 0, 0, 0%), rgba(0, 0, 0, 100%))"}}),this.rgba&&i("div",{class:`${e}-color-picker-handle`,style:{width:fe,height:fe,borderRadius:ge,left:`calc(${this.displayedSv[0]}% - ${ge})`,bottom:`calc(${this.displayedSv[1]}% - ${ge})`}},i("div",{class:`${e}-color-picker-handle__fill`,style:{backgroundColor:this.handleColor,borderRadius:ge,width:fe,height:fe}})))}}),un=ee([v("color-picker",`
 display: inline-block;
 box-sizing: border-box;
 height: var(--n-height);
 font-size: var(--n-font-size);
 width: 100%;
 position: relative;
 `),v("color-picker-panel",`
 margin: 4px 0;
 width: 240px;
 font-size: var(--n-panel-font-size);
 color: var(--n-text-color);
 background-color: var(--n-color);
 transition:
 box-shadow .3s var(--n-bezier),
 color .3s var(--n-bezier),
 background-color .3s var(--n-bezier);
 border-radius: var(--n-border-radius);
 box-shadow: var(--n-box-shadow);
 `,[ht(),v("input",`
 text-align: center;
 `)]),v("color-picker-checkboard",`
 background: white; 
 position: absolute;
 left: 0;
 right: 0;
 top: 0;
 bottom: 0;
 `,[ee("&::after",`
 background-image: linear-gradient(45deg, #DDD 25%, #0000 25%), linear-gradient(-45deg, #DDD 25%, #0000 25%), linear-gradient(45deg, #0000 75%, #DDD 75%), linear-gradient(-45deg, #0000 75%, #DDD 75%);
 background-size: 12px 12px;
 background-position: 0 0, 0 6px, 6px -6px, -6px 0px;
 background-repeat: repeat;
 content: "";
 position: absolute;
 left: 0;
 right: 0;
 top: 0;
 bottom: 0;
 `)]),v("color-picker-slider",`
 margin-bottom: 8px;
 position: relative;
 box-sizing: border-box;
 `,[U("image",`
 position: absolute;
 left: 0;
 right: 0;
 top: 0;
 bottom: 0;
 `),ee("&::after",`
 content: "";
 position: absolute;
 border-radius: inherit;
 left: 0;
 right: 0;
 top: 0;
 bottom: 0;
 box-shadow: inset 0 0 2px 0 rgba(0, 0, 0, .24);
 pointer-events: none;
 `)]),v("color-picker-handle",`
 z-index: 1;
 box-shadow: 0 0 2px 0 rgba(0, 0, 0, .45);
 position: absolute;
 background-color: white;
 overflow: hidden;
 `,[U("fill",`
 box-sizing: border-box;
 border: 2px solid white;
 `)]),v("color-picker-pallete",`
 height: 180px;
 position: relative;
 margin-bottom: 8px;
 cursor: crosshair;
 `,[U("layer",`
 position: absolute;
 left: 0;
 right: 0;
 top: 0;
 bottom: 0;
 `,[Pe("shadowed",`
 box-shadow: inset 0 0 2px 0 rgba(0, 0, 0, .24);
 `)])]),v("color-picker-preview",`
 display: flex;
 `,[U("sliders",`
 flex: 1 0 auto;
 `),U("preview",`
 position: relative;
 height: 30px;
 width: 30px;
 margin: 0 0 8px 6px;
 border-radius: 50%;
 box-shadow: rgba(0, 0, 0, .15) 0px 0px 0px 1px inset;
 overflow: hidden;
 `),U("fill",`
 display: block;
 width: 30px;
 height: 30px;
 `),U("input",`
 position: absolute;
 top: 0;
 left: 0;
 width: 30px;
 height: 30px;
 opacity: 0;
 z-index: 1;
 `)]),v("color-picker-input",`
 display: flex;
 align-items: center;
 `,[v("input",`
 flex-grow: 1;
 flex-basis: 0;
 `),U("mode",`
 width: 72px;
 text-align: center;
 `)]),v("color-picker-control",`
 padding: 12px;
 `),v("color-picker-action",`
 display: flex;
 margin-top: -4px;
 border-top: 1px solid var(--n-divider-color);
 padding: 8px 12px;
 justify-content: flex-end;
 `,[v("button","margin-left: 8px;")]),v("color-picker-trigger",`
 border: var(--n-border);
 height: 100%;
 box-sizing: border-box;
 border-radius: var(--n-border-radius);
 transition: border-color .3s var(--n-bezier);
 cursor: pointer;
 `,[U("value",`
 white-space: nowrap;
 position: relative;
 `),U("fill",`
 border-radius: var(--n-border-radius);
 position: absolute;
 display: flex;
 align-items: center;
 justify-content: center;
 left: 4px;
 right: 4px;
 top: 4px;
 bottom: 4px;
 `),Pe("disabled","cursor: not-allowed"),v("color-picker-checkboard",`
 border-radius: var(--n-border-radius);
 `,[ee("&::after",`
 --n-block-size: calc((var(--n-height) - 8px) / 3);
 background-size: calc(var(--n-block-size) * 2) calc(var(--n-block-size) * 2);
 background-position: 0 0, 0 var(--n-block-size), var(--n-block-size) calc(-1 * var(--n-block-size)), calc(-1 * var(--n-block-size)) 0px; 
 `)])]),v("color-picker-swatches",`
 display: grid;
 grid-gap: 8px;
 flex-wrap: wrap;
 position: relative;
 grid-template-columns: repeat(auto-fill, 18px);
 margin-top: 10px;
 `,[v("color-picker-swatch",`
 width: 18px;
 height: 18px;
 background-image: linear-gradient(45deg, #DDD 25%, #0000 25%), linear-gradient(-45deg, #DDD 25%, #0000 25%), linear-gradient(45deg, #0000 75%, #DDD 75%), linear-gradient(-45deg, #0000 75%, #DDD 75%);
 background-size: 8px 8px;
 background-position: 0px 0, 0px 4px, 4px -4px, -4px 0px;
 background-repeat: repeat;
 `,[U("fill",`
 position: relative;
 width: 100%;
 height: 100%;
 border-radius: 3px;
 box-shadow: rgba(0, 0, 0, .15) 0px 0px 0px 1px inset;
 cursor: pointer;
 `),ee("&:focus",`
 outline: none;
 `,[U("fill",[ee("&::after",`
 position: absolute;
 top: 0;
 right: 0;
 bottom: 0;
 left: 0;
 background: inherit;
 filter: blur(2px);
 content: "";
 `)])])])])]),cn=Object.assign(Object.assign({},je.props),{value:String,show:{type:Boolean,default:void 0},defaultShow:Boolean,defaultValue:String,modes:{type:Array,default:()=>["rgb","hex","hsl"]},placement:{type:String,default:"bottom-start"},to:$e.propTo,showAlpha:{type:Boolean,default:!0},showPreview:Boolean,swatches:Array,disabled:{type:Boolean,default:void 0},actions:{type:Array,default:null},internalActions:Array,size:String,renderLabel:Function,onComplete:Function,onConfirm:Function,onClear:Function,"onUpdate:show":[Function,Array],onUpdateShow:[Function,Array],"onUpdate:value":[Function,Array],onUpdateValue:[Function,Array]}),dn=R({name:"ColorPicker",props:cn,slots:Object,setup(e,{slots:t}){const r=w(null);let n=null;const a=mt(e),{mergedSizeRef:l,mergedDisabledRef:s}=a,{localeRef:u}=zt("global"),{mergedClsPrefixRef:p,namespaceRef:y,inlineThemeDisabled:N}=vt(e),q=je("ColorPicker","-color-picker",un,bt,e,p);xt(Me,{themeRef:q,renderLabelRef:ye(e,"renderLabel"),colorPickerSlots:t});const K=w(e.defaultShow),ue=qe(ye(e,"show"),K);function G(o){const{onUpdateShow:c,"onUpdate:show":h}=e;c&&de(c,o),h&&de(h,o),K.value=o}const{defaultValue:De}=e,ze=w(De===void 0?Tt(e.modes,e.showAlpha):De),x=qe(ye(e,"value"),ze),Y=w([x.value]),V=w(0),ve=$(()=>se(x.value)),{modes:Ke}=e,A=w(se(x.value)||Ke[0]||"rgb");function Ge(){const{modes:o}=e,{value:c}=A,h=o.findIndex(f=>f===c);~h?A.value=o[(h+1)%o.length]:A.value="rgb"}let C,S,J,W,P,I,F,_;const oe=$(()=>{const{value:o}=x;if(!o)return null;switch(ve.value){case"hsv":return X(o);case"hsl":return[C,S,J,_]=ne(o),[...Xe(C,S,J),_];case"rgb":case"hex":return[P,I,F,_]=D(o),[...Ae(P,I,F),_]}}),E=$(()=>{const{value:o}=x;if(!o)return null;switch(ve.value){case"rgb":case"hex":return D(o);case"hsv":return[C,S,W,_]=X(o),[...B(C,S,W),_];case"hsl":return[C,S,J,_]=ne(o),[...Ve(C,S,J),_]}}),be=$(()=>{const{value:o}=x;if(!o)return null;switch(ve.value){case"hsl":return ne(o);case"hsv":return[C,S,W,_]=X(o),[...me(C,S,W),_];case"rgb":case"hex":return[P,I,F,_]=D(o),[...Re(P,I,F),_]}}),Ye=$(()=>{switch(A.value){case"rgb":case"hex":return E.value;case"hsv":return oe.value;case"hsl":return be.value}}),ce=w(0),xe=w(1),ke=w([0,0]);function Je(o,c){const{value:h}=oe,f=ce.value,g=h?h[3]:1;ke.value=[o,c];const{showAlpha:d}=e;switch(A.value){case"hsv":m((d?Z:Ue)([f,o,c,g]),"cursor");break;case"hsl":m((d?H:Se)([...me(f,o,c),g]),"cursor");break;case"rgb":m((d?z:_e)([...B(f,o,c),g]),"cursor");break;case"hex":m((d?T:ie)([...B(f,o,c),g]),"cursor");break}}function We(o){ce.value=o;const{value:c}=oe;if(!c)return;const[,h,f,g]=c,{showAlpha:d}=e;switch(A.value){case"hsv":m((d?Z:Ue)([o,h,f,g]),"cursor");break;case"rgb":m((d?z:_e)([...B(o,h,f),g]),"cursor");break;case"hex":m((d?T:ie)([...B(o,h,f),g]),"cursor");break;case"hsl":m((d?H:Se)([...me(o,h,f),g]),"cursor");break}}function Qe(o){switch(A.value){case"hsv":[C,S,W]=oe.value,m(Z([C,S,W,o]),"cursor");break;case"rgb":[P,I,F]=E.value,m(z([P,I,F,o]),"cursor");break;case"hex":[P,I,F]=E.value,m(T([P,I,F,o]),"cursor");break;case"hsl":[C,S,J]=be.value,m(H([C,S,J,o]),"cursor");break}xe.value=o}function m(o,c){c==="cursor"?n=o:n=null;const{nTriggerFormChange:h,nTriggerFormInput:f}=a,{onUpdateValue:g,"onUpdate:value":d}=e;g&&de(g,o),d&&de(d,o),h(),f(),ze.value=o}function et(o){m(o,"input"),St(Q)}function Q(o=!0){const{value:c}=x;if(c){const{nTriggerFormChange:h,nTriggerFormInput:f}=a,{onComplete:g}=e;g&&g(c);const{value:d}=Y,{value:k}=V;o&&(d.splice(k+1,d.length,c),V.value=k+1),h(),f()}}function tt(){const{value:o}=V;o-1<0||(m(Y.value[o-1],"input"),Q(!1),V.value=o-1)}function nt(){const{value:o}=V;o<0||o+1>=Y.value.length||(m(Y.value[o+1],"input"),Q(!1),V.value=o+1)}function rt(){m(null,"input");const{onClear:o}=e;o&&o(),G(!1)}function at(){const{value:o}=x,{onConfirm:c}=e;c&&c(o),G(!1)}const ot=$(()=>V.value>=1),lt=$(()=>{const{value:o}=Y;return o.length>1&&V.value<o.length-1});kt(ue,o=>{o||(Y.value=[x.value],V.value=0)}),Le(()=>{if(!(n&&n===x.value)){const{value:o}=oe;o&&(ce.value=o[0],xe.value=o[3],ke.value=[o[1],o[2]])}n=null});const we=$(()=>{const{value:o}=l,{common:{cubicBezierEaseInOut:c},self:{textColor:h,color:f,panelFontSize:g,boxShadow:d,border:k,borderRadius:b,dividerColor:O,[Ie("height",o)]:st,[Ie("fontSize",o)]:ut}}=q.value;return{"--n-bezier":c,"--n-text-color":h,"--n-color":f,"--n-panel-font-size":g,"--n-font-size":ut,"--n-box-shadow":d,"--n-border":k,"--n-border-radius":b,"--n-height":st,"--n-divider-color":O}}),M=N?wt("color-picker",$(()=>l.value[0]),we,e):void 0;function it(){var o;const{value:c}=E,{value:h}=ce,{internalActions:f,modes:g,actions:d}=e,{value:k}=q,{value:b}=p;return i("div",{class:[`${b}-color-picker-panel`,M==null?void 0:M.themeClass.value],onDragstart:O=>{O.preventDefault()},style:N?void 0:we.value},i("div",{class:`${b}-color-picker-control`},i(sn,{clsPrefix:b,rgba:c,displayedHue:h,displayedSv:ke.value,onUpdateSV:Je,onComplete:Q}),i("div",{class:`${b}-color-picker-preview`},i("div",{class:`${b}-color-picker-preview__sliders`},i(ln,{clsPrefix:b,hue:h,onUpdateHue:We,onComplete:Q}),e.showAlpha?i(Lt,{clsPrefix:b,rgba:c,alpha:xe.value,onUpdateAlpha:Qe,onComplete:Q}):null),e.showPreview?i(nn,{clsPrefix:b,mode:A.value,color:E.value&&ie(E.value),onUpdateColor:O=>{m(O,"input")}}):null),i(Jt,{clsPrefix:b,showAlpha:e.showAlpha,mode:A.value,modes:g,onUpdateMode:Ge,value:x.value,valueArr:Ye.value,onUpdateValue:et}),((o=e.swatches)===null||o===void 0?void 0:o.length)&&i(en,{clsPrefix:b,mode:A.value,swatches:e.swatches,onUpdateColor:O=>{m(O,"input")}})),d!=null&&d.length?i("div",{class:`${b}-color-picker-action`},d.includes("confirm")&&i(he,{size:"small",onClick:at,theme:k.peers.Button,themeOverrides:k.peerOverrides.Button},{default:()=>u.value.confirm}),d.includes("clear")&&i(he,{size:"small",onClick:rt,disabled:!x.value,theme:k.peers.Button,themeOverrides:k.peerOverrides.Button},{default:()=>u.value.clear})):null,t.action?i("div",{class:`${b}-color-picker-action`},{default:t.action}):f?i("div",{class:`${b}-color-picker-action`},f.includes("undo")&&i(he,{size:"small",onClick:tt,disabled:!ot.value,theme:k.peers.Button,themeOverrides:k.peerOverrides.Button},{default:()=>u.value.undo}),f.includes("redo")&&i(he,{size:"small",onClick:nt,disabled:!lt.value,theme:k.peers.Button,themeOverrides:k.peerOverrides.Button},{default:()=>u.value.redo})):null)}return{mergedClsPrefix:p,namespace:y,selfRef:r,hsla:be,rgba:E,mergedShow:ue,mergedDisabled:s,isMounted:yt(),adjustedTo:$e(e),mergedValue:x,handleTriggerClick(){G(!0)},handleClickOutside(o){var c;!((c=r.value)===null||c===void 0)&&c.contains(Ct(o))||G(!1)},renderPanel:it,cssVars:N?void 0:we,themeClass:M==null?void 0:M.themeClass,onRender:M==null?void 0:M.onRender}},render(){const{mergedClsPrefix:e,onRender:t}=this;return t==null||t(),i("div",{class:[this.themeClass,`${e}-color-picker`],ref:"selfRef",style:this.cssVars},i(It,null,{default:()=>[i(Ft,null,{default:()=>i(tn,{clsPrefix:e,value:this.mergedValue,hsla:this.hsla,disabled:this.mergedDisabled,onClick:this.handleTriggerClick})}),i(Bt,{placement:this.placement,show:this.mergedShow,containerClass:this.namespace,teleportDisabled:this.adjustedTo===$e.tdkey,to:this.adjustedTo},{default:()=>i(pt,{name:"fade-in-scale-up-transition",appear:this.isMounted},{default:()=>this.mergedShow?ft(this.renderPanel(),[[gt,this.handleClickOutside,void 0,{capture:!0}]]):null})})]}))}}),hn={class:"flex-col-stretch gap-12px"},kn=R({name:"ThemeColor",__name:"theme-color",setup(e){const t=_t();function r(a,l){t.updateThemeColors(l,a)}const n=["#3b82f6","#6366f1","#8b5cf6","#a855f7","#0ea5e9","#06b6d4","#f43f5e","#ef4444","#ec4899","#d946ef","#f97316","#f59e0b","#eab308","#84cc16","#22c55e","#10b981"];return(a,l)=>{const s=Ht,u=dn;return Ce(),Fe(Ne,null,[Be(s,null,{default:He(()=>[$t(At(pe(Te)("theme.themeColor.title")),1)]),_:1}),Ut("div",hn,[(Ce(!0),Fe(Ne,null,Rt(pe(t).themeColors,(p,y)=>(Ce(),Vt(Mt,{key:y,label:pe(Te)(`theme.themeColor.${y}`)},{default:He(()=>[Be(u,{class:"w-90px",value:pe(t).themeColors[y],"show-alpha":!1,swatches:n,"onUpdate:value":N=>r(N,y)},null,8,["value","onUpdate:value"])]),_:2},1032,["label"]))),128))])],64)}}});export{kn as _};
