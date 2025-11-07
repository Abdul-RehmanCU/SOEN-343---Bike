export const GridBackground = ({ gridSize = "48px" }) => (
  <div 
    className="absolute inset-0 bg-[linear-gradient(to_right,#80808012_1px,transparent_1px),linear-gradient(to_bottom,#80808012_1px,transparent_1px)] dark:bg-[linear-gradient(to_right,#ffffff20_1px,transparent_1px),linear-gradient(to_bottom,#ffffff20_1px,transparent_1px)]"
    style={{ backgroundSize: `${gridSize} ${gridSize}` }}
  />
);