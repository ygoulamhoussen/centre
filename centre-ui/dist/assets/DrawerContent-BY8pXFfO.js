import{d as A,w as W,D as s,b9 as me,T as V,ao as ge,ba as q,v as N,O as E,a as $,E as K,aC as ve,S as J,am as we,a5 as pe,bb as ye,b4 as Se,bc as $e,bd as U,J as P,be as ze,bf as Ce,bg as xe,A as i,bh as I,z as h,C as y,B,bi as Be,bj as ke,bk as Ee,b6 as Re,H as G,bl as Oe,L,bm as Te,M as Fe,bn as He,ap as k,bo as Me,bp as Pe}from"./index-D_-DzWhp.js";import{u as X}from"./use-merged-state-DuV1Lu3L.js";import{f as Y}from"./Popover-uiGA841y.js";const Ie=A({name:"NDrawerContent",inheritAttrs:!1,props:{blockScroll:Boolean,show:{type:Boolean,default:void 0},displayDirective:{type:String,required:!0},placement:{type:String,required:!0},contentClass:String,contentStyle:[Object,String],nativeScrollbar:{type:Boolean,required:!0},scrollbarProps:Object,trapFocus:{type:Boolean,default:!0},autoFocus:{type:Boolean,default:!0},showMask:{type:[Boolean,String],required:!0},maxWidth:Number,maxHeight:Number,minWidth:Number,minHeight:Number,resizable:Boolean,onClickoutside:Function,onAfterLeave:Function,onAfterEnter:Function,onEsc:Function},setup(e){const t=E(!!e.show),r=E(null),g=J(U);let f=0,S="",c=null;const v=E(!1),w=E(!1),z=$(()=>e.placement==="top"||e.placement==="bottom"),{mergedClsPrefixRef:R,mergedRtlRef:O}=K(e),T=ve("Drawer",O,R),C=n,b=o=>{w.value=!0,f=z.value?o.clientY:o.clientX,S=document.body.style.cursor,document.body.style.cursor=z.value?"ns-resize":"ew-resize",document.body.addEventListener("mousemove",p),document.body.addEventListener("mouseleave",C),document.body.addEventListener("mouseup",n)},H=()=>{c!==null&&(window.clearTimeout(c),c=null),w.value?v.value=!0:c=window.setTimeout(()=>{v.value=!0},300)},D=()=>{c!==null&&(window.clearTimeout(c),c=null),v.value=!1},{doUpdateHeight:j,doUpdateWidth:_}=g,F=o=>{const{maxWidth:l}=e;if(l&&o>l)return l;const{minWidth:u}=e;return u&&o<u?u:o},M=o=>{const{maxHeight:l}=e;if(l&&o>l)return l;const{minHeight:u}=e;return u&&o<u?u:o};function p(o){var l,u;if(w.value)if(z.value){let m=((l=r.value)===null||l===void 0?void 0:l.offsetHeight)||0;const x=f-o.clientY;m+=e.placement==="bottom"?x:-x,m=M(m),j(m),f=o.clientY}else{let m=((u=r.value)===null||u===void 0?void 0:u.offsetWidth)||0;const x=f-o.clientX;m+=e.placement==="right"?x:-x,m=F(m),_(m),f=o.clientX}}function n(){w.value&&(f=0,w.value=!1,document.body.style.cursor=S,document.body.removeEventListener("mousemove",p),document.body.removeEventListener("mouseup",n),document.body.removeEventListener("mouseleave",C))}we(()=>{e.show&&(t.value=!0)}),pe(()=>e.show,o=>{o||n()}),ye(()=>{n()});const a=$(()=>{const{show:o}=e,l=[[N,o]];return e.showMask||l.push([Se,e.onClickoutside,void 0,{capture:!0}]),l});function d(){var o;t.value=!1,(o=e.onAfterLeave)===null||o===void 0||o.call(e)}return $e($(()=>e.blockScroll&&t.value)),P(ze,r),P(Ce,null),P(xe,null),{bodyRef:r,rtlEnabled:T,mergedClsPrefix:g.mergedClsPrefixRef,isMounted:g.isMountedRef,mergedTheme:g.mergedThemeRef,displayed:t,transitionName:$(()=>({right:"slide-in-from-right-transition",left:"slide-in-from-left-transition",top:"slide-in-from-top-transition",bottom:"slide-in-from-bottom-transition"})[e.placement]),handleAfterLeave:d,bodyDirectives:a,handleMousedownResizeTrigger:b,handleMouseenterResizeTrigger:H,handleMouseleaveResizeTrigger:D,isDragging:w,isHoverOnResizeTrigger:v}},render(){const{$slots:e,mergedClsPrefix:t}=this;return this.displayDirective==="show"||this.displayed||this.show?W(s("div",{role:"none"},s(me,{disabled:!this.showMask||!this.trapFocus,active:this.show,autoFocus:this.autoFocus,onEsc:this.onEsc},{default:()=>s(V,{name:this.transitionName,appear:this.isMounted,onAfterEnter:this.onAfterEnter,onAfterLeave:this.handleAfterLeave},{default:()=>W(s("div",ge(this.$attrs,{role:"dialog",ref:"bodyRef","aria-modal":"true",class:[`${t}-drawer`,this.rtlEnabled&&`${t}-drawer--rtl`,`${t}-drawer--${this.placement}-placement`,this.isDragging&&`${t}-drawer--unselectable`,this.nativeScrollbar&&`${t}-drawer--native-scrollbar`]}),[this.resizable?s("div",{class:[`${t}-drawer__resize-trigger`,(this.isDragging||this.isHoverOnResizeTrigger)&&`${t}-drawer__resize-trigger--hover`],onMouseenter:this.handleMouseenterResizeTrigger,onMouseleave:this.handleMouseleaveResizeTrigger,onMousedown:this.handleMousedownResizeTrigger}):null,this.nativeScrollbar?s("div",{class:[`${t}-drawer-content-wrapper`,this.contentClass],style:this.contentStyle,role:"none"},e):s(q,Object.assign({},this.scrollbarProps,{contentStyle:this.contentStyle,contentClass:[`${t}-drawer-content-wrapper`,this.contentClass],theme:this.mergedTheme.peers.Scrollbar,themeOverrides:this.mergedTheme.peerOverrides.Scrollbar}),e)]),this.bodyDirectives)})})),[[N,this.displayDirective==="if"||this.displayed||this.show]]):null}}),{cubicBezierEaseIn:De,cubicBezierEaseOut:je}=I;function _e({duration:e="0.3s",leaveDuration:t="0.2s",name:r="slide-in-from-bottom"}={}){return[i(`&.${r}-transition-leave-active`,{transition:`transform ${t} ${De}`}),i(`&.${r}-transition-enter-active`,{transition:`transform ${e} ${je}`}),i(`&.${r}-transition-enter-to`,{transform:"translateY(0)"}),i(`&.${r}-transition-enter-from`,{transform:"translateY(100%)"}),i(`&.${r}-transition-leave-from`,{transform:"translateY(0)"}),i(`&.${r}-transition-leave-to`,{transform:"translateY(100%)"})]}const{cubicBezierEaseIn:We,cubicBezierEaseOut:Ae}=I;function Ue({duration:e="0.3s",leaveDuration:t="0.2s",name:r="slide-in-from-left"}={}){return[i(`&.${r}-transition-leave-active`,{transition:`transform ${t} ${We}`}),i(`&.${r}-transition-enter-active`,{transition:`transform ${e} ${Ae}`}),i(`&.${r}-transition-enter-to`,{transform:"translateX(0)"}),i(`&.${r}-transition-enter-from`,{transform:"translateX(-100%)"}),i(`&.${r}-transition-leave-from`,{transform:"translateX(0)"}),i(`&.${r}-transition-leave-to`,{transform:"translateX(-100%)"})]}const{cubicBezierEaseIn:Ne,cubicBezierEaseOut:Le}=I;function Xe({duration:e="0.3s",leaveDuration:t="0.2s",name:r="slide-in-from-right"}={}){return[i(`&.${r}-transition-leave-active`,{transition:`transform ${t} ${Ne}`}),i(`&.${r}-transition-enter-active`,{transition:`transform ${e} ${Le}`}),i(`&.${r}-transition-enter-to`,{transform:"translateX(0)"}),i(`&.${r}-transition-enter-from`,{transform:"translateX(100%)"}),i(`&.${r}-transition-leave-from`,{transform:"translateX(0)"}),i(`&.${r}-transition-leave-to`,{transform:"translateX(100%)"})]}const{cubicBezierEaseIn:Ye,cubicBezierEaseOut:Ve}=I;function qe({duration:e="0.3s",leaveDuration:t="0.2s",name:r="slide-in-from-top"}={}){return[i(`&.${r}-transition-leave-active`,{transition:`transform ${t} ${Ye}`}),i(`&.${r}-transition-enter-active`,{transition:`transform ${e} ${Ve}`}),i(`&.${r}-transition-enter-to`,{transform:"translateY(0)"}),i(`&.${r}-transition-enter-from`,{transform:"translateY(-100%)"}),i(`&.${r}-transition-leave-from`,{transform:"translateY(0)"}),i(`&.${r}-transition-leave-to`,{transform:"translateY(-100%)"})]}const Ke=i([h("drawer",`
 word-break: break-word;
 line-height: var(--n-line-height);
 position: absolute;
 pointer-events: all;
 box-shadow: var(--n-box-shadow);
 transition:
 background-color .3s var(--n-bezier),
 color .3s var(--n-bezier);
 background-color: var(--n-color);
 color: var(--n-text-color);
 box-sizing: border-box;
 `,[Xe(),Ue(),qe(),_e(),y("unselectable",`
 user-select: none; 
 -webkit-user-select: none;
 `),y("native-scrollbar",[h("drawer-content-wrapper",`
 overflow: auto;
 height: 100%;
 `)]),B("resize-trigger",`
 position: absolute;
 background-color: #0000;
 transition: background-color .3s var(--n-bezier);
 `,[y("hover",`
 background-color: var(--n-resize-trigger-color-hover);
 `)]),h("drawer-content-wrapper",`
 box-sizing: border-box;
 `),h("drawer-content",`
 height: 100%;
 display: flex;
 flex-direction: column;
 `,[y("native-scrollbar",[h("drawer-body-content-wrapper",`
 height: 100%;
 overflow: auto;
 `)]),h("drawer-body",`
 flex: 1 0 0;
 overflow: hidden;
 `),h("drawer-body-content-wrapper",`
 box-sizing: border-box;
 padding: var(--n-body-padding);
 `),h("drawer-header",`
 font-weight: var(--n-title-font-weight);
 line-height: 1;
 font-size: var(--n-title-font-size);
 color: var(--n-title-text-color);
 padding: var(--n-header-padding);
 transition: border .3s var(--n-bezier);
 border-bottom: 1px solid var(--n-divider-color);
 border-bottom: var(--n-header-border-bottom);
 display: flex;
 justify-content: space-between;
 align-items: center;
 `,[B("main",`
 flex: 1;
 `),B("close",`
 margin-left: 6px;
 transition:
 background-color .3s var(--n-bezier),
 color .3s var(--n-bezier);
 `)]),h("drawer-footer",`
 display: flex;
 justify-content: flex-end;
 border-top: var(--n-footer-border-top);
 transition: border .3s var(--n-bezier);
 padding: var(--n-footer-padding);
 `)]),y("right-placement",`
 top: 0;
 bottom: 0;
 right: 0;
 border-top-left-radius: var(--n-border-radius);
 border-bottom-left-radius: var(--n-border-radius);
 `,[B("resize-trigger",`
 width: 3px;
 height: 100%;
 top: 0;
 left: 0;
 transform: translateX(-1.5px);
 cursor: ew-resize;
 `)]),y("left-placement",`
 top: 0;
 bottom: 0;
 left: 0;
 border-top-right-radius: var(--n-border-radius);
 border-bottom-right-radius: var(--n-border-radius);
 `,[B("resize-trigger",`
 width: 3px;
 height: 100%;
 top: 0;
 right: 0;
 transform: translateX(1.5px);
 cursor: ew-resize;
 `)]),y("top-placement",`
 top: 0;
 left: 0;
 right: 0;
 border-bottom-left-radius: var(--n-border-radius);
 border-bottom-right-radius: var(--n-border-radius);
 `,[B("resize-trigger",`
 width: 100%;
 height: 3px;
 bottom: 0;
 left: 0;
 transform: translateY(1.5px);
 cursor: ns-resize;
 `)]),y("bottom-placement",`
 left: 0;
 bottom: 0;
 right: 0;
 border-top-left-radius: var(--n-border-radius);
 border-top-right-radius: var(--n-border-radius);
 `,[B("resize-trigger",`
 width: 100%;
 height: 3px;
 top: 0;
 left: 0;
 transform: translateY(-1.5px);
 cursor: ns-resize;
 `)])]),i("body",[i(">",[h("drawer-container",`
 position: fixed;
 `)])]),h("drawer-container",`
 position: relative;
 position: absolute;
 left: 0;
 right: 0;
 top: 0;
 bottom: 0;
 pointer-events: none;
 `,[i("> *",`
 pointer-events: all;
 `)]),h("drawer-mask",`
 background-color: rgba(0, 0, 0, .3);
 position: absolute;
 left: 0;
 right: 0;
 top: 0;
 bottom: 0;
 `,[y("invisible",`
 background-color: rgba(0, 0, 0, 0)
 `),Be({enterDuration:"0.2s",leaveDuration:"0.2s",enterCubicBezier:"var(--n-bezier-in)",leaveCubicBezier:"var(--n-bezier-out)"})])]),Je=Object.assign(Object.assign({},G.props),{show:Boolean,width:[Number,String],height:[Number,String],placement:{type:String,default:"right"},maskClosable:{type:Boolean,default:!0},showMask:{type:[Boolean,String],default:!0},to:[String,Object],displayDirective:{type:String,default:"if"},nativeScrollbar:{type:Boolean,default:!0},zIndex:Number,onMaskClick:Function,scrollbarProps:Object,contentClass:String,contentStyle:[Object,String],trapFocus:{type:Boolean,default:!0},onEsc:Function,autoFocus:{type:Boolean,default:!0},closeOnEsc:{type:Boolean,default:!0},blockScroll:{type:Boolean,default:!0},maxWidth:Number,maxHeight:Number,minWidth:Number,minHeight:Number,resizable:Boolean,defaultWidth:{type:[Number,String],default:251},defaultHeight:{type:[Number,String],default:251},onUpdateWidth:[Function,Array],onUpdateHeight:[Function,Array],"onUpdate:width":[Function,Array],"onUpdate:height":[Function,Array],"onUpdate:show":[Function,Array],onUpdateShow:[Function,Array],onAfterEnter:Function,onAfterLeave:Function,drawerStyle:[String,Object],drawerClass:String,target:null,onShow:Function,onHide:Function}),tt=A({name:"Drawer",inheritAttrs:!1,props:Je,setup(e){const{mergedClsPrefixRef:t,namespaceRef:r,inlineThemeDisabled:g}=K(e),f=Re(),S=G("Drawer","-drawer",Ke,Oe,e,t),c=E(e.defaultWidth),v=E(e.defaultHeight),w=X(L(e,"width"),c),z=X(L(e,"height"),v),R=$(()=>{const{placement:n}=e;return n==="top"||n==="bottom"?"":Y(w.value)}),O=$(()=>{const{placement:n}=e;return n==="left"||n==="right"?"":Y(z.value)}),T=n=>{const{onUpdateWidth:a,"onUpdate:width":d}=e;a&&k(a,n),d&&k(d,n),c.value=n},C=n=>{const{onUpdateHeight:a,"onUpdate:width":d}=e;a&&k(a,n),d&&k(d,n),v.value=n},b=$(()=>[{width:R.value,height:O.value},e.drawerStyle||""]);function H(n){const{onMaskClick:a,maskClosable:d}=e;d&&F(!1),a&&a(n)}function D(n){H(n)}const j=Te();function _(n){var a;(a=e.onEsc)===null||a===void 0||a.call(e),e.show&&e.closeOnEsc&&He(n)&&(j.value||F(!1))}function F(n){const{onHide:a,onUpdateShow:d,"onUpdate:show":o}=e;d&&k(d,n),o&&k(o,n),a&&!n&&k(a,n)}P(U,{isMountedRef:f,mergedThemeRef:S,mergedClsPrefixRef:t,doUpdateShow:F,doUpdateHeight:C,doUpdateWidth:T});const M=$(()=>{const{common:{cubicBezierEaseInOut:n,cubicBezierEaseIn:a,cubicBezierEaseOut:d},self:{color:o,textColor:l,boxShadow:u,lineHeight:m,headerPadding:x,footerPadding:Q,borderRadius:Z,bodyPadding:ee,titleFontSize:te,titleTextColor:re,titleFontWeight:ne,headerBorderBottom:oe,footerBorderTop:ie,closeIconColor:se,closeIconColorHover:ae,closeIconColorPressed:le,closeColorHover:de,closeColorPressed:ce,closeIconSize:ue,closeSize:he,closeBorderRadius:fe,resizableTriggerColorHover:be}}=S.value;return{"--n-line-height":m,"--n-color":o,"--n-border-radius":Z,"--n-text-color":l,"--n-box-shadow":u,"--n-bezier":n,"--n-bezier-out":d,"--n-bezier-in":a,"--n-header-padding":x,"--n-body-padding":ee,"--n-footer-padding":Q,"--n-title-text-color":re,"--n-title-font-size":te,"--n-title-font-weight":ne,"--n-header-border-bottom":oe,"--n-footer-border-top":ie,"--n-close-icon-color":se,"--n-close-icon-color-hover":ae,"--n-close-icon-color-pressed":le,"--n-close-size":he,"--n-close-color-hover":de,"--n-close-color-pressed":ce,"--n-close-icon-size":ue,"--n-close-border-radius":fe,"--n-resize-trigger-color-hover":be}}),p=g?Fe("drawer",void 0,M,e):void 0;return{mergedClsPrefix:t,namespace:r,mergedBodyStyle:b,handleOutsideClick:D,handleMaskClick:H,handleEsc:_,mergedTheme:S,cssVars:g?void 0:M,themeClass:p==null?void 0:p.themeClass,onRender:p==null?void 0:p.onRender,isMounted:f}},render(){const{mergedClsPrefix:e}=this;return s(ke,{to:this.to,show:this.show},{default:()=>{var t;return(t=this.onRender)===null||t===void 0||t.call(this),W(s("div",{class:[`${e}-drawer-container`,this.namespace,this.themeClass],style:this.cssVars,role:"none"},this.showMask?s(V,{name:"fade-in-transition",appear:this.isMounted},{default:()=>this.show?s("div",{"aria-hidden":!0,class:[`${e}-drawer-mask`,this.showMask==="transparent"&&`${e}-drawer-mask--invisible`],onClick:this.handleMaskClick}):null}):null,s(Ie,Object.assign({},this.$attrs,{class:[this.drawerClass,this.$attrs.class],style:[this.mergedBodyStyle,this.$attrs.style],blockScroll:this.blockScroll,contentStyle:this.contentStyle,contentClass:this.contentClass,placement:this.placement,scrollbarProps:this.scrollbarProps,show:this.show,displayDirective:this.displayDirective,nativeScrollbar:this.nativeScrollbar,onAfterEnter:this.onAfterEnter,onAfterLeave:this.onAfterLeave,trapFocus:this.trapFocus,autoFocus:this.autoFocus,resizable:this.resizable,maxHeight:this.maxHeight,minHeight:this.minHeight,maxWidth:this.maxWidth,minWidth:this.minWidth,showMask:this.showMask,onEsc:this.handleEsc,onClickoutside:this.handleOutsideClick}),this.$slots)),[[Ee,{zIndex:this.zIndex,enabled:this.show}]])}})}}),Ge={title:String,headerClass:String,headerStyle:[Object,String],footerClass:String,footerStyle:[Object,String],bodyClass:String,bodyStyle:[Object,String],bodyContentClass:String,bodyContentStyle:[Object,String],nativeScrollbar:{type:Boolean,default:!0},scrollbarProps:Object,closable:Boolean},rt=A({name:"DrawerContent",props:Ge,slots:Object,setup(){const e=J(U,null);e||Pe("drawer-content","`n-drawer-content` must be placed inside `n-drawer`.");const{doUpdateShow:t}=e;function r(){t(!1)}return{handleCloseClick:r,mergedTheme:e.mergedThemeRef,mergedClsPrefix:e.mergedClsPrefixRef}},render(){const{title:e,mergedClsPrefix:t,nativeScrollbar:r,mergedTheme:g,bodyClass:f,bodyStyle:S,bodyContentClass:c,bodyContentStyle:v,headerClass:w,headerStyle:z,footerClass:R,footerStyle:O,scrollbarProps:T,closable:C,$slots:b}=this;return s("div",{role:"none",class:[`${t}-drawer-content`,r&&`${t}-drawer-content--native-scrollbar`]},b.header||e||C?s("div",{class:[`${t}-drawer-header`,w],style:z,role:"none"},s("div",{class:`${t}-drawer-header__main`,role:"heading","aria-level":"1"},b.header!==void 0?b.header():e),C&&s(Me,{onClick:this.handleCloseClick,clsPrefix:t,class:`${t}-drawer-header__close`,absolute:!0})):null,r?s("div",{class:[`${t}-drawer-body`,f],style:S,role:"none"},s("div",{class:[`${t}-drawer-body-content-wrapper`,c],style:v,role:"none"},b)):s(q,Object.assign({themeOverrides:g.peerOverrides.Scrollbar,theme:g.peers.Scrollbar},T,{class:`${t}-drawer-body`,contentClass:[`${t}-drawer-body-content-wrapper`,c],contentStyle:v}),b),b.footer?s("div",{class:[`${t}-drawer-footer`,R],style:O,role:"none"},b.footer()):null)}});export{rt as _,tt as a};
