import{aZ as Z,aG as q,A as T,z as B,ac as Q,B as ee,C as J,bH as ge,d as ue,D as p,T as be,E as we,H as ae,O as k,am as fe,a as H,S as me,b4 as ie,v as ye,M as Se,bb as $e,a5 as Ce,L as V,b7 as se,bI as Be,aI as le,b9 as xe,ao as Me,w as he,aA as te,F as Re,bv as Te,J as U,bf as ke,be as ze,bg as _e,bJ as Oe,bG as Ee,bK as Ie,bk as Ae,ai as Pe,b6 as je,ah as de,ap as K}from"./index-D_-DzWhp.js";import{m as Fe}from"./map-BMPY2oeZ.js";import{a as De,u as X,B as Ne,V as We}from"./Follower-CPJzzLbY.js";import{u as He}from"./use-merged-state-DuV1Lu3L.js";import{u as Le}from"./use-compitable-Cr7qxnTd.js";const A="@@mmoContext",Ke={mounted(e,{value:n}){e[A]={handler:void 0},typeof n=="function"&&(e[A].handler=n,q("mousemoveoutside",e,n))},updated(e,{value:n}){const r=e[A];typeof n=="function"?r.handler?r.handler!==n&&(Z("mousemoveoutside",e,r.handler),r.handler=n,q("mousemoveoutside",e,n)):(e[A].handler=n,q("mousemoveoutside",e,n)):r.handler&&(Z("mousemoveoutside",e,r.handler),r.handler=void 0)},unmounted(e){const{handler:n}=e[A];n&&Z("mousemoveoutside",e,n),e[A].handler=void 0}},Ve=/^(\d|\.)+$/,ce=/(\d|\.)+/;function ne(e,{c:n=1,offset:r=0,attachPx:o=!0}={}){if(typeof e=="number"){const a=(e+r)*n;return a===0?"0":`${a}px`}else if(typeof e=="string")if(Ve.test(e)){const a=(Number(e)+r)*n;return o?a===0?"0":`${a}px`:`${a}`}else{const a=ce.exec(e);return a?e.replace(ce,String((Number(a[0])+r)*n)):e}return e}let oe;function Ue(){return oe===void 0&&(oe=navigator.userAgent.includes("Node.js")||navigator.userAgent.includes("jsdom")),oe}const re={top:"bottom",bottom:"top",left:"right",right:"left"},f="var(--n-arrow-height) * 1.414",Je=T([B("popover",`
 transition:
 box-shadow .3s var(--n-bezier),
 background-color .3s var(--n-bezier),
 color .3s var(--n-bezier);
 position: relative;
 font-size: var(--n-font-size);
 color: var(--n-text-color);
 box-shadow: var(--n-box-shadow);
 word-break: break-word;
 `,[T(">",[B("scrollbar",`
 height: inherit;
 max-height: inherit;
 `)]),Q("raw",`
 background-color: var(--n-color);
 border-radius: var(--n-border-radius);
 `,[Q("scrollable",[Q("show-header-or-footer","padding: var(--n-padding);")])]),ee("header",`
 padding: var(--n-padding);
 border-bottom: 1px solid var(--n-divider-color);
 transition: border-color .3s var(--n-bezier);
 `),ee("footer",`
 padding: var(--n-padding);
 border-top: 1px solid var(--n-divider-color);
 transition: border-color .3s var(--n-bezier);
 `),J("scrollable, show-header-or-footer",[ee("content",`
 padding: var(--n-padding);
 `)])]),B("popover-shared",`
 transform-origin: inherit;
 `,[B("popover-arrow-wrapper",`
 position: absolute;
 overflow: hidden;
 pointer-events: none;
 `,[B("popover-arrow",`
 transition: background-color .3s var(--n-bezier);
 position: absolute;
 display: block;
 width: calc(${f});
 height: calc(${f});
 box-shadow: 0 0 8px 0 rgba(0, 0, 0, .12);
 transform: rotate(45deg);
 background-color: var(--n-color);
 pointer-events: all;
 `)]),T("&.popover-transition-enter-from, &.popover-transition-leave-to",`
 opacity: 0;
 transform: scale(.85);
 `),T("&.popover-transition-enter-to, &.popover-transition-leave-from",`
 transform: scale(1);
 opacity: 1;
 `),T("&.popover-transition-enter-active",`
 transition:
 box-shadow .3s var(--n-bezier),
 background-color .3s var(--n-bezier),
 color .3s var(--n-bezier),
 opacity .15s var(--n-bezier-ease-out),
 transform .15s var(--n-bezier-ease-out);
 `),T("&.popover-transition-leave-active",`
 transition:
 box-shadow .3s var(--n-bezier),
 background-color .3s var(--n-bezier),
 color .3s var(--n-bezier),
 opacity .15s var(--n-bezier-ease-in),
 transform .15s var(--n-bezier-ease-in);
 `)]),S("top-start",`
 top: calc(${f} / -2);
 left: calc(${C("top-start")} - var(--v-offset-left));
 `),S("top",`
 top: calc(${f} / -2);
 transform: translateX(calc(${f} / -2)) rotate(45deg);
 left: 50%;
 `),S("top-end",`
 top: calc(${f} / -2);
 right: calc(${C("top-end")} + var(--v-offset-left));
 `),S("bottom-start",`
 bottom: calc(${f} / -2);
 left: calc(${C("bottom-start")} - var(--v-offset-left));
 `),S("bottom",`
 bottom: calc(${f} / -2);
 transform: translateX(calc(${f} / -2)) rotate(45deg);
 left: 50%;
 `),S("bottom-end",`
 bottom: calc(${f} / -2);
 right: calc(${C("bottom-end")} + var(--v-offset-left));
 `),S("left-start",`
 left: calc(${f} / -2);
 top: calc(${C("left-start")} - var(--v-offset-top));
 `),S("left",`
 left: calc(${f} / -2);
 transform: translateY(calc(${f} / -2)) rotate(45deg);
 top: 50%;
 `),S("left-end",`
 left: calc(${f} / -2);
 bottom: calc(${C("left-end")} + var(--v-offset-top));
 `),S("right-start",`
 right: calc(${f} / -2);
 top: calc(${C("right-start")} - var(--v-offset-top));
 `),S("right",`
 right: calc(${f} / -2);
 transform: translateY(calc(${f} / -2)) rotate(45deg);
 top: 50%;
 `),S("right-end",`
 right: calc(${f} / -2);
 bottom: calc(${C("right-end")} + var(--v-offset-top));
 `),...Fe({top:["right-start","left-start"],right:["top-end","bottom-end"],bottom:["right-end","left-end"],left:["top-start","bottom-start"]},(e,n)=>{const r=["right","left"].includes(n),o=r?"width":"height";return e.map(a=>{const c=a.split("-")[1]==="end",i=`calc((${`var(--v-target-${o}, 0px)`} - ${f}) / 2)`,d=C(a);return T(`[v-placement="${a}"] >`,[B("popover-shared",[J("center-arrow",[B("popover-arrow",`${n}: calc(max(${i}, ${d}) ${c?"+":"-"} var(--v-offset-${r?"left":"top"}));`)])])])})})]);function C(e){return["top","bottom"].includes(e.split("-")[0])?"var(--n-arrow-offset)":"var(--n-arrow-offset-vertical)"}function S(e,n){const r=e.split("-")[0],o=["top","bottom"].includes(r)?"height: var(--n-space-arrow);":"width: var(--n-space-arrow);";return T(`[v-placement="${e}"] >`,[B("popover-shared",`
 margin-${re[r]}: var(--n-space);
 `,[J("show-arrow",`
 margin-${re[r]}: var(--n-space-arrow);
 `),J("overlap",`
 margin: 0;
 `),ge("popover-arrow-wrapper",`
 right: 0;
 left: 0;
 top: 0;
 bottom: 0;
 ${r}: 100%;
 ${re[r]}: auto;
 ${o}
 `,[B("popover-arrow",n)])])])}const ve=Object.assign(Object.assign({},ae.props),{to:X.propTo,show:Boolean,trigger:String,showArrow:Boolean,delay:Number,duration:Number,raw:Boolean,arrowPointToCenter:Boolean,arrowClass:String,arrowStyle:[String,Object],arrowWrapperClass:String,arrowWrapperStyle:[String,Object],displayDirective:String,x:Number,y:Number,flip:Boolean,overlap:Boolean,placement:String,width:[Number,String],keepAliveOnHover:Boolean,scrollable:Boolean,contentClass:String,contentStyle:[Object,String],headerClass:String,headerStyle:[Object,String],footerClass:String,footerStyle:[Object,String],internalDeactivateImmediately:Boolean,animated:Boolean,onClickoutside:Function,internalTrapFocus:Boolean,internalOnAfterLeave:Function,minWidth:Number,maxWidth:Number});function Xe({arrowClass:e,arrowStyle:n,arrowWrapperClass:r,arrowWrapperStyle:o,clsPrefix:a}){return p("div",{key:"__popover-arrow__",style:o,class:[`${a}-popover-arrow-wrapper`,r]},p("div",{class:[`${a}-popover-arrow`,e],style:n}))}const Ge=ue({name:"PopoverBody",inheritAttrs:!1,props:ve,setup(e,{slots:n,attrs:r}){const{namespaceRef:o,mergedClsPrefixRef:a,inlineThemeDisabled:c}=we(e),u=ae("Popover","-popover",Je,Be,e,a),i=k(null),d=me("NPopover"),b=k(null),w=k(e.show),x=k(!1);fe(()=>{const{show:s}=e;s&&!Ue()&&!e.internalDeactivateImmediately&&(x.value=!0)});const z=H(()=>{const{trigger:s,onClickoutside:g}=e,m=[],{positionManuallyRef:{value:l}}=d;return l||(s==="click"&&!g&&m.push([ie,L,void 0,{capture:!0}]),s==="hover"&&m.push([Ke,Y])),g&&m.push([ie,L,void 0,{capture:!0}]),(e.displayDirective==="show"||e.animated&&x.value)&&m.push([ye,e.show]),m}),M=H(()=>{const{common:{cubicBezierEaseInOut:s,cubicBezierEaseIn:g,cubicBezierEaseOut:m},self:{space:l,spaceArrow:D,padding:N,fontSize:R,textColor:W,dividerColor:t,color:v,boxShadow:y,borderRadius:E,arrowHeight:I,arrowOffset:$,arrowOffsetVertical:pe}}=u.value;return{"--n-box-shadow":y,"--n-bezier":s,"--n-bezier-ease-in":g,"--n-bezier-ease-out":m,"--n-font-size":R,"--n-text-color":W,"--n-color":v,"--n-divider-color":t,"--n-border-radius":E,"--n-arrow-height":I,"--n-arrow-offset":$,"--n-arrow-offset-vertical":pe,"--n-padding":N,"--n-space":l,"--n-space-arrow":D}}),P=H(()=>{const s=e.width==="trigger"?void 0:ne(e.width),g=[];s&&g.push({width:s});const{maxWidth:m,minWidth:l}=e;return m&&g.push({maxWidth:ne(m)}),l&&g.push({maxWidth:ne(l)}),c||g.push(M.value),g}),h=c?Se("popover",void 0,M,e):void 0;d.setBodyInstance({syncPosition:G}),$e(()=>{d.setBodyInstance(null)}),Ce(V(e,"show"),s=>{e.animated||(s?w.value=!0:w.value=!1)});function G(){var s;(s=i.value)===null||s===void 0||s.syncPosition()}function _(s){e.trigger==="hover"&&e.keepAliveOnHover&&e.show&&d.handleMouseEnter(s)}function O(s){e.trigger==="hover"&&e.keepAliveOnHover&&d.handleMouseLeave(s)}function Y(s){e.trigger==="hover"&&!j().contains(se(s))&&d.handleMouseMoveOutside(s)}function L(s){(e.trigger==="click"&&!j().contains(se(s))||e.onClickoutside)&&d.handleClickOutside(s)}function j(){return d.getTriggerElement()}U(ke,b),U(ze,null),U(_e,null);function F(){if(h==null||h.onRender(),!(e.displayDirective==="show"||e.show||e.animated&&x.value))return null;let g;const m=d.internalRenderBodyRef.value,{value:l}=a;if(m)g=m([`${l}-popover-shared`,h==null?void 0:h.themeClass.value,e.overlap&&`${l}-popover-shared--overlap`,e.showArrow&&`${l}-popover-shared--show-arrow`,e.arrowPointToCenter&&`${l}-popover-shared--center-arrow`],b,P.value,_,O);else{const{value:D}=d.extraClassRef,{internalTrapFocus:N}=e,R=!le(n.header)||!le(n.footer),W=()=>{var t,v;const y=R?p(Re,null,te(n.header,$=>$?p("div",{class:[`${l}-popover__header`,e.headerClass],style:e.headerStyle},$):null),te(n.default,$=>$?p("div",{class:[`${l}-popover__content`,e.contentClass],style:e.contentStyle},n):null),te(n.footer,$=>$?p("div",{class:[`${l}-popover__footer`,e.footerClass],style:e.footerStyle},$):null)):e.scrollable?(t=n.default)===null||t===void 0?void 0:t.call(n):p("div",{class:[`${l}-popover__content`,e.contentClass],style:e.contentStyle},n),E=e.scrollable?p(Te,{contentClass:R?void 0:`${l}-popover__content ${(v=e.contentClass)!==null&&v!==void 0?v:""}`,contentStyle:R?void 0:e.contentStyle},{default:()=>y}):y,I=e.showArrow?Xe({arrowClass:e.arrowClass,arrowStyle:e.arrowStyle,arrowWrapperClass:e.arrowWrapperClass,arrowWrapperStyle:e.arrowWrapperStyle,clsPrefix:l}):null;return[E,I]};g=p("div",Me({class:[`${l}-popover`,`${l}-popover-shared`,h==null?void 0:h.themeClass.value,D.map(t=>`${l}-${t}`),{[`${l}-popover--scrollable`]:e.scrollable,[`${l}-popover--show-header-or-footer`]:R,[`${l}-popover--raw`]:e.raw,[`${l}-popover-shared--overlap`]:e.overlap,[`${l}-popover-shared--show-arrow`]:e.showArrow,[`${l}-popover-shared--center-arrow`]:e.arrowPointToCenter}],ref:b,style:P.value,onKeydown:d.handleKeydown,onMouseenter:_,onMouseleave:O},r),N?p(xe,{active:e.show,autoFocus:!0},{default:W}):W())}return he(g,z.value)}return{displayed:x,namespace:o,isMounted:d.isMountedRef,zIndex:d.zIndexRef,followerRef:i,adjustedTo:X(e),followerEnabled:w,renderContentNode:F}},render(){return p(De,{ref:"followerRef",zIndex:this.zIndex,show:this.show,enabled:this.followerEnabled,to:this.adjustedTo,x:this.x,y:this.y,flip:this.flip,placement:this.placement,containerClass:this.namespace,overlap:this.overlap,width:this.width==="trigger"?"target":void 0,teleportDisabled:this.adjustedTo===X.tdkey},{default:()=>this.animated?p(be,{name:"popover-transition",appear:this.isMounted,onEnter:()=>{this.followerEnabled=!0},onAfterLeave:()=>{var e;(e=this.internalOnAfterLeave)===null||e===void 0||e.call(this),this.followerEnabled=!1,this.displayed=!1}},{default:this.renderContentNode}):this.renderContentNode()})}}),Ye=Object.keys(ve),Ze={focus:["onFocus","onBlur"],click:["onClick"],hover:["onMouseenter","onMouseleave"],manual:[],nested:["onFocus","onBlur","onMouseenter","onMouseleave","onClick"]};function qe(e,n,r){Ze[n].forEach(o=>{e.props?e.props=Object.assign({},e.props):e.props={};const a=e.props[o],c=r[o];a?e.props[o]=(...u)=>{a(...u),c(...u)}:e.props[o]=c})}const Qe={show:{type:Boolean,default:void 0},defaultShow:Boolean,showArrow:{type:Boolean,default:!0},trigger:{type:String,default:"hover"},delay:{type:Number,default:100},duration:{type:Number,default:100},raw:Boolean,placement:{type:String,default:"top"},x:Number,y:Number,arrowPointToCenter:Boolean,disabled:Boolean,getDisabled:Function,displayDirective:{type:String,default:"if"},arrowClass:String,arrowStyle:[String,Object],arrowWrapperClass:String,arrowWrapperStyle:[String,Object],flip:{type:Boolean,default:!0},animated:{type:Boolean,default:!0},width:{type:[Number,String],default:void 0},overlap:Boolean,keepAliveOnHover:{type:Boolean,default:!0},zIndex:Number,to:X.propTo,scrollable:Boolean,contentClass:String,contentStyle:[Object,String],headerClass:String,headerStyle:[Object,String],footerClass:String,footerStyle:[Object,String],onClickoutside:Function,"onUpdate:show":[Function,Array],onUpdateShow:[Function,Array],internalDeactivateImmediately:Boolean,internalSyncTargetWithParent:Boolean,internalInheritedEventHandlers:{type:Array,default:()=>[]},internalTrapFocus:Boolean,internalExtraClass:{type:Array,default:()=>[]},onShow:[Function,Array],onHide:[Function,Array],arrow:{type:Boolean,default:void 0},minWidth:Number,maxWidth:Number},et=Object.assign(Object.assign(Object.assign({},ae.props),Qe),{internalOnAfterLeave:Function,internalRenderBody:Function}),it=ue({name:"Popover",inheritAttrs:!1,props:et,slots:Object,__popover__:!0,setup(e){const n=je(),r=k(null),o=H(()=>e.show),a=k(e.defaultShow),c=He(o,a),u=de(()=>e.disabled?!1:c.value),i=()=>{if(e.disabled)return!0;const{getDisabled:t}=e;return!!(t!=null&&t())},d=()=>i()?!1:c.value,b=Le(e,["arrow","showArrow"]),w=H(()=>e.overlap?!1:b.value);let x=null;const z=k(null),M=k(null),P=de(()=>e.x!==void 0&&e.y!==void 0);function h(t){const{"onUpdate:show":v,onUpdateShow:y,onShow:E,onHide:I}=e;a.value=t,v&&K(v,t),y&&K(y,t),t&&E&&K(E,!0),t&&I&&K(I,!1)}function G(){x&&x.syncPosition()}function _(){const{value:t}=z;t&&(window.clearTimeout(t),z.value=null)}function O(){const{value:t}=M;t&&(window.clearTimeout(t),M.value=null)}function Y(){const t=i();if(e.trigger==="focus"&&!t){if(d())return;h(!0)}}function L(){const t=i();if(e.trigger==="focus"&&!t){if(!d())return;h(!1)}}function j(){const t=i();if(e.trigger==="hover"&&!t){if(O(),z.value!==null||d())return;const v=()=>{h(!0),z.value=null},{delay:y}=e;y===0?v():z.value=window.setTimeout(v,y)}}function F(){const t=i();if(e.trigger==="hover"&&!t){if(_(),M.value!==null||!d())return;const v=()=>{h(!1),M.value=null},{duration:y}=e;y===0?v():M.value=window.setTimeout(v,y)}}function s(){F()}function g(t){var v;d()&&(e.trigger==="click"&&(_(),O(),h(!1)),(v=e.onClickoutside)===null||v===void 0||v.call(e,t))}function m(){if(e.trigger==="click"&&!i()){_(),O();const t=!d();h(t)}}function l(t){e.internalTrapFocus&&t.key==="Escape"&&(_(),O(),h(!1))}function D(t){a.value=t}function N(){var t;return(t=r.value)===null||t===void 0?void 0:t.targetRef}function R(t){x=t}return U("NPopover",{getTriggerElement:N,handleKeydown:l,handleMouseEnter:j,handleMouseLeave:F,handleClickOutside:g,handleMouseMoveOutside:s,setBodyInstance:R,positionManuallyRef:P,isMountedRef:n,zIndexRef:V(e,"zIndex"),extraClassRef:V(e,"internalExtraClass"),internalRenderBodyRef:V(e,"internalRenderBody")}),fe(()=>{c.value&&i()&&h(!1)}),{binderInstRef:r,positionManually:P,mergedShowConsideringDisabledProp:u,uncontrolledShow:a,mergedShowArrow:w,getMergedShow:d,setShow:D,handleClick:m,handleMouseEnter:j,handleMouseLeave:F,handleFocus:Y,handleBlur:L,syncPosition:G}},render(){var e;const{positionManually:n,$slots:r}=this;let o,a=!1;if(!n&&(o=Oe(r,"trigger"),o)){o=Ee(o),o=o.type===Ie?p("span",[o]):o;const c={onClick:this.handleClick,onMouseenter:this.handleMouseEnter,onMouseleave:this.handleMouseLeave,onFocus:this.handleFocus,onBlur:this.handleBlur};if(!((e=o.type)===null||e===void 0)&&e.__popover__)a=!0,o.props||(o.props={internalSyncTargetWithParent:!0,internalInheritedEventHandlers:[]}),o.props.internalSyncTargetWithParent=!0,o.props.internalInheritedEventHandlers?o.props.internalInheritedEventHandlers=[c,...o.props.internalInheritedEventHandlers]:o.props.internalInheritedEventHandlers=[c];else{const{internalInheritedEventHandlers:u}=this,i=[c,...u],d={onBlur:b=>{i.forEach(w=>{w.onBlur(b)})},onFocus:b=>{i.forEach(w=>{w.onFocus(b)})},onClick:b=>{i.forEach(w=>{w.onClick(b)})},onMouseenter:b=>{i.forEach(w=>{w.onMouseenter(b)})},onMouseleave:b=>{i.forEach(w=>{w.onMouseleave(b)})}};qe(o,u?"nested":n?"manual":this.trigger,d)}}return p(Ne,{ref:"binderInstRef",syncTarget:!a,syncTargetWithParent:this.internalSyncTargetWithParent},{default:()=>{this.mergedShowConsideringDisabledProp;const c=this.getMergedShow();return[this.internalTrapFocus&&c?he(p("div",{style:{position:"fixed",top:0,right:0,bottom:0,left:0}}),[[Ae,{enabled:c,zIndex:this.zIndex}]]):null,n?null:p(We,null,{default:()=>o}),p(Ge,Pe(this.$props,Ye,Object.assign(Object.assign({},this.$attrs),{showArrow:this.mergedShowArrow,show:c})),{default:()=>{var u,i;return(i=(u=this.$slots).default)===null||i===void 0?void 0:i.call(u)},header:()=>{var u,i;return(i=(u=this.$slots).header)===null||i===void 0?void 0:i.call(u)},footer:()=>{var u,i;return(i=(u=this.$slots).footer)===null||i===void 0?void 0:i.call(u)}})]}})}});export{it as _,ne as f,Qe as p,Xe as r};
