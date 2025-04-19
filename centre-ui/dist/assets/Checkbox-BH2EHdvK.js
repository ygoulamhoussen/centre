import{d as K,D as s,E as H,aB as j,O as U,a as _,J as se,L as N,N as ue,ap as l,A as b,z as a,cy as be,cz as fe,C as S,B as C,aH as he,aA as ke,aJ as ve,aG as me,ah as ge,S as xe,H as E,cW as pe,aC as Ce,aL as P,M as ye,an as we}from"./index-D_-DzWhp.js";import{u as G}from"./use-merged-state-DuV1Lu3L.js";const L=ue("n-checkbox-group"),ze={min:Number,max:Number,size:String,value:Array,defaultValue:{type:Array,default:null},disabled:{type:Boolean,default:void 0},"onUpdate:value":[Function,Array],onUpdateValue:[Function,Array],onChange:[Function,Array]},Be=K({name:"CheckboxGroup",props:ze,setup(o){const{mergedClsPrefixRef:d}=H(o),g=j(o),{mergedSizeRef:y,mergedDisabledRef:w}=g,x=U(o.defaultValue),T=_(()=>o.value),f=G(T,x),D=_(()=>{var u;return((u=f.value)===null||u===void 0?void 0:u.length)||0}),n=_(()=>Array.isArray(f.value)?new Set(f.value):new Set);function z(u,r){const{nTriggerFormInput:p,nTriggerFormChange:h}=g,{onChange:c,"onUpdate:value":k,onUpdateValue:v}=o;if(Array.isArray(f.value)){const i=Array.from(f.value),M=i.findIndex(I=>I===r);u?~M||(i.push(r),v&&l(v,i,{actionType:"check",value:r}),k&&l(k,i,{actionType:"check",value:r}),p(),h(),x.value=i,c&&l(c,i)):~M&&(i.splice(M,1),v&&l(v,i,{actionType:"uncheck",value:r}),k&&l(k,i,{actionType:"uncheck",value:r}),c&&l(c,i),x.value=i,p(),h())}else u?(v&&l(v,[r],{actionType:"check",value:r}),k&&l(k,[r],{actionType:"check",value:r}),c&&l(c,[r]),x.value=[r],p(),h()):(v&&l(v,[],{actionType:"uncheck",value:r}),k&&l(k,[],{actionType:"uncheck",value:r}),c&&l(c,[]),x.value=[],p(),h())}return se(L,{checkedCountRef:D,maxRef:N(o,"max"),minRef:N(o,"min"),valueSetRef:n,disabledRef:w,mergedSizeRef:y,toggleCheckbox:z}),{mergedClsPrefix:d}},render(){return s("div",{class:`${this.mergedClsPrefix}-checkbox-group`,role:"group"},this.$slots)}}),Re=()=>s("svg",{viewBox:"0 0 64 64",class:"check-icon"},s("path",{d:"M50.42,16.76L22.34,39.45l-8.1-11.46c-1.12-1.58-3.3-1.96-4.88-0.84c-1.58,1.12-1.95,3.3-0.84,4.88l10.26,14.51  c0.56,0.79,1.42,1.31,2.38,1.45c0.16,0.02,0.32,0.03,0.48,0.03c0.8,0,1.57-0.27,2.2-0.78l30.99-25.03c1.5-1.21,1.74-3.42,0.52-4.92  C54.13,15.78,51.93,15.55,50.42,16.76z"})),Se=()=>s("svg",{viewBox:"0 0 100 100",class:"line-icon"},s("path",{d:"M80.2,55.5H21.4c-2.8,0-5.1-2.5-5.1-5.5l0,0c0-3,2.3-5.5,5.1-5.5h58.7c2.8,0,5.1,2.5,5.1,5.5l0,0C85.2,53.1,82.9,55.5,80.2,55.5z"})),Te=b([a("checkbox",`
 font-size: var(--n-font-size);
 outline: none;
 cursor: pointer;
 display: inline-flex;
 flex-wrap: nowrap;
 align-items: flex-start;
 word-break: break-word;
 line-height: var(--n-size);
 --n-merged-color-table: var(--n-color-table);
 `,[S("show-label","line-height: var(--n-label-line-height);"),b("&:hover",[a("checkbox-box",[C("border","border: var(--n-border-checked);")])]),b("&:focus:not(:active)",[a("checkbox-box",[C("border",`
 border: var(--n-border-focus);
 box-shadow: var(--n-box-shadow-focus);
 `)])]),S("inside-table",[a("checkbox-box",`
 background-color: var(--n-merged-color-table);
 `)]),S("checked",[a("checkbox-box",`
 background-color: var(--n-color-checked);
 `,[a("checkbox-icon",[b(".check-icon",`
 opacity: 1;
 transform: scale(1);
 `)])])]),S("indeterminate",[a("checkbox-box",[a("checkbox-icon",[b(".check-icon",`
 opacity: 0;
 transform: scale(.5);
 `),b(".line-icon",`
 opacity: 1;
 transform: scale(1);
 `)])])]),S("checked, indeterminate",[b("&:focus:not(:active)",[a("checkbox-box",[C("border",`
 border: var(--n-border-checked);
 box-shadow: var(--n-box-shadow-focus);
 `)])]),a("checkbox-box",`
 background-color: var(--n-color-checked);
 border-left: 0;
 border-top: 0;
 `,[C("border",{border:"var(--n-border-checked)"})])]),S("disabled",{cursor:"not-allowed"},[S("checked",[a("checkbox-box",`
 background-color: var(--n-color-disabled-checked);
 `,[C("border",{border:"var(--n-border-disabled-checked)"}),a("checkbox-icon",[b(".check-icon, .line-icon",{fill:"var(--n-check-mark-color-disabled-checked)"})])])]),a("checkbox-box",`
 background-color: var(--n-color-disabled);
 `,[C("border",`
 border: var(--n-border-disabled);
 `),a("checkbox-icon",[b(".check-icon, .line-icon",`
 fill: var(--n-check-mark-color-disabled);
 `)])]),C("label",`
 color: var(--n-text-color-disabled);
 `)]),a("checkbox-box-wrapper",`
 position: relative;
 width: var(--n-size);
 flex-shrink: 0;
 flex-grow: 0;
 user-select: none;
 -webkit-user-select: none;
 `),a("checkbox-box",`
 position: absolute;
 left: 0;
 top: 50%;
 transform: translateY(-50%);
 height: var(--n-size);
 width: var(--n-size);
 display: inline-block;
 box-sizing: border-box;
 border-radius: var(--n-border-radius);
 background-color: var(--n-color);
 transition: background-color 0.3s var(--n-bezier);
 `,[C("border",`
 transition:
 border-color .3s var(--n-bezier),
 box-shadow .3s var(--n-bezier);
 border-radius: inherit;
 position: absolute;
 left: 0;
 right: 0;
 top: 0;
 bottom: 0;
 border: var(--n-border);
 `),a("checkbox-icon",`
 display: flex;
 align-items: center;
 justify-content: center;
 position: absolute;
 left: 1px;
 right: 1px;
 top: 1px;
 bottom: 1px;
 `,[b(".check-icon, .line-icon",`
 width: 100%;
 fill: var(--n-check-mark-color);
 opacity: 0;
 transform: scale(0.5);
 transform-origin: center;
 transition:
 fill 0.3s var(--n-bezier),
 transform 0.3s var(--n-bezier),
 opacity 0.3s var(--n-bezier),
 border-color 0.3s var(--n-bezier);
 `),he({left:"1px",top:"1px"})])]),C("label",`
 color: var(--n-text-color);
 transition: color .3s var(--n-bezier);
 user-select: none;
 -webkit-user-select: none;
 padding: var(--n-label-padding);
 font-weight: var(--n-label-font-weight);
 `,[b("&:empty",{display:"none"})])]),be(a("checkbox",`
 --n-merged-color-table: var(--n-color-table-modal);
 `)),fe(a("checkbox",`
 --n-merged-color-table: var(--n-color-table-popover);
 `))]),De=Object.assign(Object.assign({},E.props),{size:String,checked:{type:[Boolean,String,Number],default:void 0},defaultChecked:{type:[Boolean,String,Number],default:!1},value:[String,Number],disabled:{type:Boolean,default:void 0},indeterminate:Boolean,label:String,focusable:{type:Boolean,default:!0},checkedValue:{type:[Boolean,String,Number],default:!0},uncheckedValue:{type:[Boolean,String,Number],default:!1},"onUpdate:checked":[Function,Array],onUpdateChecked:[Function,Array],privateInsideTable:Boolean,onChange:[Function,Array]}),Me=K({name:"Checkbox",props:De,setup(o){const d=xe(L,null),g=U(null),{mergedClsPrefixRef:y,inlineThemeDisabled:w,mergedRtlRef:x}=H(o),T=U(o.defaultChecked),f=N(o,"checked"),D=G(f,T),n=ge(()=>{if(d){const e=d.valueSetRef.value;return e&&o.value!==void 0?e.has(o.value):!1}else return D.value===o.checkedValue}),z=j(o,{mergedSize(e){const{size:m}=o;if(m!==void 0)return m;if(d){const{value:t}=d.mergedSizeRef;if(t!==void 0)return t}if(e){const{mergedSize:t}=e;if(t!==void 0)return t.value}return"medium"},mergedDisabled(e){const{disabled:m}=o;if(m!==void 0)return m;if(d){if(d.disabledRef.value)return!0;const{maxRef:{value:t},checkedCountRef:R}=d;if(t!==void 0&&R.value>=t&&!n.value)return!0;const{minRef:{value:A}}=d;if(A!==void 0&&R.value<=A&&n.value)return!0}return e?e.disabled.value:!1}}),{mergedDisabledRef:u,mergedSizeRef:r}=z,p=E("Checkbox","-checkbox",Te,pe,o,y);function h(e){if(d&&o.value!==void 0)d.toggleCheckbox(!n.value,o.value);else{const{onChange:m,"onUpdate:checked":t,onUpdateChecked:R}=o,{nTriggerFormInput:A,nTriggerFormChange:F}=z,B=n.value?o.uncheckedValue:o.checkedValue;t&&l(t,B,e),R&&l(R,B,e),m&&l(m,B,e),A(),F(),T.value=B}}function c(e){u.value||h(e)}function k(e){if(!u.value)switch(e.key){case" ":case"Enter":h(e)}}function v(e){switch(e.key){case" ":e.preventDefault()}}const i={focus:()=>{var e;(e=g.value)===null||e===void 0||e.focus()},blur:()=>{var e;(e=g.value)===null||e===void 0||e.blur()}},M=Ce("Checkbox",x,y),I=_(()=>{const{value:e}=r,{common:{cubicBezierEaseInOut:m},self:{borderRadius:t,color:R,colorChecked:A,colorDisabled:F,colorTableHeader:B,colorTableHeaderModal:O,colorTableHeaderPopover:V,checkMarkColor:W,checkMarkColorDisabled:J,border:Y,borderFocus:q,borderDisabled:Q,borderChecked:X,boxShadowFocus:Z,textColor:ee,textColorDisabled:oe,checkMarkColorDisabledChecked:ne,colorDisabledChecked:re,borderDisabledChecked:ae,labelPadding:ce,labelLineHeight:le,labelFontWeight:de,[P("fontSize",e)]:ie,[P("size",e)]:te}}=p.value;return{"--n-label-line-height":le,"--n-label-font-weight":de,"--n-size":te,"--n-bezier":m,"--n-border-radius":t,"--n-border":Y,"--n-border-checked":X,"--n-border-focus":q,"--n-border-disabled":Q,"--n-border-disabled-checked":ae,"--n-box-shadow-focus":Z,"--n-color":R,"--n-color-checked":A,"--n-color-table":B,"--n-color-table-modal":O,"--n-color-table-popover":V,"--n-color-disabled":F,"--n-color-disabled-checked":re,"--n-text-color":ee,"--n-text-color-disabled":oe,"--n-check-mark-color":W,"--n-check-mark-color-disabled":J,"--n-check-mark-color-disabled-checked":ne,"--n-font-size":ie,"--n-label-padding":ce}}),$=w?ye("checkbox",_(()=>r.value[0]),I,o):void 0;return Object.assign(z,i,{rtlEnabled:M,selfRef:g,mergedClsPrefix:y,mergedDisabled:u,renderedChecked:n,mergedTheme:p,labelId:we(),handleClick:c,handleKeyUp:k,handleKeyDown:v,cssVars:w?void 0:I,themeClass:$==null?void 0:$.themeClass,onRender:$==null?void 0:$.onRender})},render(){var o;const{$slots:d,renderedChecked:g,mergedDisabled:y,indeterminate:w,privateInsideTable:x,cssVars:T,labelId:f,label:D,mergedClsPrefix:n,focusable:z,handleKeyUp:u,handleKeyDown:r,handleClick:p}=this;(o=this.onRender)===null||o===void 0||o.call(this);const h=ke(d.default,c=>D||c?s("span",{class:`${n}-checkbox__label`,id:f},D||c):null);return s("div",{ref:"selfRef",class:[`${n}-checkbox`,this.themeClass,this.rtlEnabled&&`${n}-checkbox--rtl`,g&&`${n}-checkbox--checked`,y&&`${n}-checkbox--disabled`,w&&`${n}-checkbox--indeterminate`,x&&`${n}-checkbox--inside-table`,h&&`${n}-checkbox--show-label`],tabindex:y||!z?void 0:0,role:"checkbox","aria-checked":w?"mixed":g,"aria-labelledby":f,style:T,onKeyup:u,onKeydown:r,onClick:p,onMousedown:()=>{me("selectstart",window,c=>{c.preventDefault()},{once:!0})}},s("div",{class:`${n}-checkbox-box-wrapper`}," ",s("div",{class:`${n}-checkbox-box`},s(ve,null,{default:()=>this.indeterminate?s("div",{key:"indeterminate",class:`${n}-checkbox-icon`},Se()):s("div",{key:"check",class:`${n}-checkbox-icon`},Re())}),s("div",{class:`${n}-checkbox-box__border`}))),h)}});export{Be as N,Me as _};
