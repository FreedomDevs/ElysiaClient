{ pkgs ? import <nixpkgs> {} }:

pkgs.mkShell {
  packages = with pkgs; [
    jdk25

    libGL
    mesa
    libglvnd
  ];

  shellHook = ''
    export JAVA_HOME="${pkgs.jdk25}"
    export LD_LIBRARY_PATH="${pkgs.lib.makeLibraryPath [
      pkgs.libGL
      pkgs.mesa
      pkgs.libglvnd
    ]}:$LD_LIBRARY_PATH"
  '';
}